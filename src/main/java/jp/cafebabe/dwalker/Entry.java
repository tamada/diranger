package jp.cafebabe.dwalker;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;

public class Entry {
    private final Path path;
    private BasicFileAttributes attributes;
    private final FileSystemProvider provider;

    public Entry(Path path, BasicFileAttributes attributes, FileSystemProvider provider) {
        this.path = path;
        this.attributes = attributes;
        this.provider = provider;
    }

    public Entry(Path path, FileSystemProvider provider) {
        this.path = path;
        this.provider = provider;
    }

    public boolean isDirectory() {
        if(attributes == null)
            findAttributes();
        return attributes.isDirectory();
    }

    private void findAttributes() {
        try {
            this.attributes = provider.readAttributes(path, BasicFileAttributes.class);
        } catch(IOException e) {
        }
    }

    public boolean isSymlink() {
        if(attributes == null)
            findAttributes();
        return attributes.isSymbolicLink();
    }

    public boolean isHidden() {
        try {
            return provider.isHidden(path());
        } catch(IOException e) {
            return true;
        }
    }

    public DirectoryStream<Path> newDirectoryStream(Config config) throws IOException {
        return provider.newDirectoryStream(path(), config.buildFilter(provider));
    }

    public Path path() {
        return path;
    }

    public FileSystemProvider provider() {
        return provider;
    }

    public BasicFileAttributes attributes() {
        if(attributes == null)
            findAttributes();
        return attributes;
    }
}
