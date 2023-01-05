package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import jp.cafebabe.diranger.Entry;
import jp.cafebabe.diranger.Ranger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ParallelModel implements Ranger {
    @Override
    public Stream<Entry> stream(Entry base, Config config) throws IOException {
        var spliterator = Spliterators.spliteratorUnknownSize(iterator(base, config), 0);
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public Iterator<Entry> iterator(Entry base, Config config) throws IOException {
        return visit(base, config);
    }

    private Visitor visit(Entry base, Config config) {
        var visitor = new Visitor();
        Executors.newWorkStealingPool()
                .submit(() -> execute(base, config, visitor));
        return visitor;
    }

    public void execute(Entry base, Config config, FileVisitor<Entry> visitor) {
        TreeWalker walker = new TreeWalker(base, config);
        try {
            walker.accept(visitor);
        } catch(IOException e) {
            LoggerFactory.getLogger(getClass()).warn("I/O error", e);
        }
    }

    private static class Visitor implements FileVisitor<Entry>, Iterator<Entry> {
        private final BlockingQueue<Entry> queue = new LinkedBlockingQueue<>();
        private Entry base;
        private boolean hasNext = true;

        private final Lock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition();

        @Override
        public boolean hasNext() {
            awaitUntilFindFirstElementOrEmptyDir();
            return hasNext || queue.size() > 0;
        }

        @Override
        public Entry next() {
            awaitUntilFindFirstElementOrEmptyDir();
            if(hasNext || queue.size() > 0) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    LoggerFactory.getLogger(getClass())
                            .warn("error on taking a entry in the queue", e);
                }
            }
            throw new NoSuchElementException("no more entry");
        }

        private synchronized void awaitUntilFindFirstElementOrEmptyDir() {
            lock.lock();
            try {
                if (hasNext && queue.size() == 0) {
                    notEmpty.await();
                }
            } catch(InterruptedException e) {
                    LoggerFactory.getLogger(getClass())
                            .warn("error on awaiting the thread", e);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public FileVisitResult preVisitDirectory(Entry dir, BasicFileAttributes attrs) throws IOException {
            if(base == null)
                base = dir;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Entry file, BasicFileAttributes attrs) throws IOException {
            lock.lock();
            try {
                queue.put(file);
                notEmpty.signal();
            } catch(InterruptedException e) {
                LoggerFactory.getLogger(getClass()).warn("I/O error", e);
            } finally {
                lock.unlock();
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Entry file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Entry dir, IOException exc) throws IOException {
            lock.lock();
            try {
                if (Files.isSameFile(base.path(), dir.path())) {
                    hasNext = false;
                    notEmpty.signal();
                }
                return FileVisitResult.CONTINUE;
            } finally {
                lock.unlock();
            }
        }
    }
}
