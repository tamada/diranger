package jp.cafebabe.diranger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Entry implements Comparable<Entry> {
    private final Path path;
    private final BasicFileAttributes attributes;
    private final FileSystemProvider provider;
    private boolean exist = true;

    public Entry(Path path, BasicFileAttributes attributes, FileSystemProvider provider) {
        this.path = path;
        this.provider = provider;
        this.attributes = attributes == null? findAttributes(): attributes;
    }

    public Entry(Path path, FileSystemProvider provider) {
        this.path = path;
        this.provider = provider;
        this.attributes = findAttributes();
    }

    public Entry(Path path) {
        this(path, path.getFileSystem().provider());
    }

    public boolean exists() {
        return exist;
    }

    public boolean isDirectory() {
        if(attributes == null)
            return false;
        return attributes.isDirectory();
    }

    public boolean isSymlink() {
        if(attributes == null)
            return false;
        return attributes.isSymbolicLink();
    }

    public boolean isHidden() {
        try {
            return provider.isHidden(path());
        } catch(IOException e) {
            return true;
        }
    }

    public Stream<Entry> list(Config config) throws IOException {
        var ds = provider.newDirectoryStream(path(), config.buildFilter(provider));
        return StreamSupport.stream(ds.spliterator(), false)
                .map(p -> new Entry(p, provider));
    }

    public Path path() {
        return path;
    }

    public FileSystemProvider provider() {
        return provider;
    }

    public BasicFileAttributes attributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return path.toString();
    }

    @Override
    public int compareTo(Entry o) {
        return path().compareTo(o.path());
    }

    private BasicFileAttributes findAttributes() {
        try {
            return provider.readAttributes(path, BasicFileAttributes.class);
        } catch(IOException e) {
            exist = false;
        }
        return null;
    }

}
