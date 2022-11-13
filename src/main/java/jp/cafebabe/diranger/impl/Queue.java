package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import jp.cafebabe.diranger.Entry;
import jp.cafebabe.diranger.Ranger;
import jp.cafebabe.diranger.TreeWalker;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.stream.Stream;

public class Queue implements Ranger {

    @Override
    public Stream<Entry> stream(Entry base, Config config) throws IOException {
        var visitor = visit(base, config);
        return visitor.queue.stream();
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
            e.printStackTrace();
        }
    }

    private static final class Visitor implements FileVisitor<Entry> {
        private final TransferQueue<Entry> queue = new LinkedTransferQueue<>();
        private boolean hasNext = true;
        private Entry base;

        public boolean hasNext() {
            return queue.size() > 0 && hasNext;
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
                e.printStackTrace();
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
