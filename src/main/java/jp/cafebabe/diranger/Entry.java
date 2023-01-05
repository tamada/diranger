package jp.cafebabe.diranger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class represents an entry in a directory.
 * An object of the class shows the path of a file, or a directory.
 * 
 * @author Haruaki TAMADA
 */
public class Entry implements Comparable<Entry> {
    private final Path path;
    private final BasicFileAttributes attributes;
    private final FileSystemProvider provider;

    private Entry(Path path, FileSystemProvider provider) {
        this.path = path;
        this.provider = provider;
        this.attributes = findAttributes();
    }

    public boolean isDirectory() {
        if(attributes == null)
            return false;
        return attributes.isDirectory();
    }

    public Stream<Entry> list(Config config) throws IOException {
        var ds = provider.newDirectoryStream(path(), config.buildFilter(provider));
        return StreamSupport.stream(ds.spliterator(), false)
                .sorted()
                .map(p -> new Entry(p, provider));
    }

    public Entry parent() {
        Path parent = path().getParent();
        if(parent == null)
            return null;
        return Entry.of(parent, provider);
    }
    public Path relativize(Entry path) {
        return path().relativize(path.path());
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
    public Entry resolve(String path) {
        return resolve(Path.of(path));
    }

    public Entry resolve(Path sub) {
        return Entry.of(path().resolve(sub), provider());
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
            return null;
        }
    }

    public boolean isSame(Path path) {
        try {
            return provider().isSameFile(path(), path);
        } catch(IOException e) {
            return false;
        }
    }

    public static Entry of(String path) {
        return of(Path.of(path));
    }

    public static Entry of(Path path) {
        return new Entry(path, path.getFileSystem().provider());
    }

    public static Entry of(Path path, FileSystemProvider provider) {
        return new Entry(path, provider);
    }
}
