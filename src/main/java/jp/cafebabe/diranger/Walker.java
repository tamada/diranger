package jp.cafebabe.diranger;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

public interface Walker {
    Stream<Path> stream();

    Iterator<Path> iterator();
}
