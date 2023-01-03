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
        var walker = new TreeWalker(config, base);
        var visitor = new Visitor();
        var wrapper = new VisitorFactory(config).create(visitor);
        walker.accept(wrapper);
        return visitor.list.stream();
    }

    private static final class Visitor implements FileVisitor<Entry> {
        private final List<Entry> list = new ArrayList<>();

        @Override
        public FileVisitResult preVisitDirectory(Entry dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Entry file, BasicFileAttributes attrs) throws IOException {
            list.add(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Entry file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Entry dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
