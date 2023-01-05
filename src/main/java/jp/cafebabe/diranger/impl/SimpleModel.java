package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import jp.cafebabe.diranger.Entry;
import jp.cafebabe.diranger.Ranger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SimpleModel implements Ranger {
    public Stream<Entry> stream(Entry base, Config config) throws IOException {
        var walker = new TreeWalker(base, config);
        var visitor = new Visitor();
        walker.accept(visitor);
        return visitor.list.stream();
    }

    private static final class Visitor implements FileVisitor<Entry> {
        private final List<Entry> list = new ArrayList<>();

        @Override
        public FileVisitResult preVisitDirectory(Entry dir, BasicFileAttributes attrs) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Entry file, BasicFileAttributes attrs) {
            list.add(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Entry file, IOException exc) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Entry dir, IOException exc) {
            return FileVisitResult.CONTINUE;
        }
    }
}
