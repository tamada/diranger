package jp.cafebabe.traverser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

public class FileItem {
    private Path path;
    private Optional<BasicFileAttributes> attributes;
    private Optional<IOException> exception;

    private FileItem(Path path, BasicFileAttributes attributes, IOException exception) {
        this.path = path;
        this.attributes = Optional.ofNullable(attributes);
        this.exception = Optional.ofNullable(exception);
    }

    public Path path() {
        return path;
    }

    public boolean isFailed() {
        return exception.isPresent();
    }

    public Optional<IOException> exception() {
        return exception;
    }

    public Optional<BasicFileAttributes> attributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return path.toString();
    }

    public static FileItem fail(Path path, IOException exception) {
        return new FileItem(path, null, exception);
    }

    static FileItem build(Path path, DefaultTraverser traverser) {
        try {
            return new FileItem(path, traverser.attributes(path), null);
        } catch (IOException e) {
            return FileItem.fail(path, e);
        }
    }
}
