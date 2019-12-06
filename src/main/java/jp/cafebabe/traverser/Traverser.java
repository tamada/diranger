package jp.cafebabe.traverser;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.swing.filechooser.FileFilter;

public interface Traverser {
    Stream<Path> stream(Filter filter) throws IOException;

    Path basePath();

    default Stream<Path> stream(DirectoryStream.Filter<Path> streamFilter) throws IOException{
        return stream(map(streamFilter));
    }

    default Stream<Path> stream(java.io.FileFilter fileFilter) throws IOException{
        return stream(map(fileFilter));
    }

    default Stream<Path> stream(FileFilter fileFilter) throws IOException{
        return stream(map(fileFilter));
    }

    default Stream<Path> stream() throws IOException{
        return stream((Filter)(path -> true));
    }

    static Filter map(java.io.FileFilter filter) {
        return path -> filter.accept(path.toFile());
    }

    static Filter map(javax.swing.filechooser.FileFilter filter) {
        return path -> filter.accept(path.toFile());
    }

    static Filter map(DirectoryStream.Filter<Path> filter) {
        return path -> filter.accept(path);
    }
}
