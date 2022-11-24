package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import jp.cafebabe.diranger.Entry;
import jp.cafebabe.diranger.Ranger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Queue implements Ranger {
    @Override
    public Stream<Entry> stream(Entry base, Config config) throws IOException {
        var spliterator = Spliterators.spliteratorUnknownSize(iterator(base, config), 0);
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public Iterator<Entry> iterator(Entry base, Config config) throws IOException {
        return visit(base, config);
    }

    private Visitor visit(Entry base, Config config) throws IOException {
        var visitor = new Visitor();
        var future = Executors.newWorkStealingPool()
                .submit(() -> execute(config, base, visitor));
        return visitor;
    }

    private void execute(Config config, Entry base, Visitor visitor) {
        TreeWalker walker = new TreeWalker(config, base);
        try {
            walker.accept(visitor);
        } catch(IOException e) {
            LoggerFactory.getLogger(getClass())
                            .warn("I/O error", e);
        }
    }

    private static final class Visitor implements FileVisitor<Entry>, Iterator<Entry> {
        private final TransferQueue<Entry> queue = new LinkedTransferQueue<>();
        private boolean hasNext = true;
        private Entry base;

        @Override
        public Entry next() {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                LoggerFactory.getLogger(getClass())
                        .warn("error on taking a entry in the queue", e);
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return queue.size() > 0 || hasNext;
        }

        @Override
        public FileVisitResult preVisitDirectory(Entry dir, BasicFileAttributes attrs) throws IOException {
            if(base == null)
                base = dir;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Entry file, BasicFileAttributes attrs) throws IOException {
            try {
                queue.put(file);
            } catch(InterruptedException e) {
                LoggerFactory.getLogger(getClass()).warn("I/O error", e);;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Entry file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Entry dir, IOException exc) throws IOException {
            if(base == dir)
                hasNext = false;
            return FileVisitResult.CONTINUE;
        }
    }
}
