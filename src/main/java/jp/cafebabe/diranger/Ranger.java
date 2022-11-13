package jp.cafebabe.diranger;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Ranger {
    Stream<Entry> stream(Entry base, Config config) throws IOException;

    default Stream<Entry> stream(Path base, Config config) throws IOException {
        return stream(Entry.of(base), config);
    }

    default Iterator<Entry> iterator(Path base, Config config) throws IOException {
        return stream(base, config)
                .iterator();
    }

    default List<Entry> list(Entry base, Config config) throws IOException {
        return stream(base, config)
                .collect(Collectors.toList());
    }
}
