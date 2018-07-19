package com.github.traverser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;

public class FileItem {
    private Path path;
    private BasicFileAttributes attributes;

    private FileItem(Path path, BasicFileAttributes attributes) {
        this.path = path;
        this.attributes = attributes;
    }

    public Path path() {
        return path;
    }

    public boolean isFailed() {
        return attributes == null;
    }

    public BasicFileAttributes attributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return path.toString();
    }

    public static FileItem fail(Path path) {
        return new FileItem(path, null);
    }

    public static FileItem build(Path path, FileSystemProvider provider) {
        try {
            return new FileItem(path, provider.readAttributes(path, BasicFileAttributes.class));
        } catch (IOException e) {
            return FileItem.fail(path);
        }
    }
}
