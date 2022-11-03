package jp.cafebabe.dwalker;

import com.github.traverser.Filter;
import com.github.traverser.TraverserLogger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DefaultWalker implements Walker {
    private Path basePath;
    private FileSystemProvider provider;
    private FileSystem system;

    public Iterator<Path> iterator() {
    }

    public Stream<Path> stream() {
        DirectoryStream<Path> stream = provider.newDirectoryStream()
    }

    private Stream<Path> traversePath(Path path, Filter filter){
        try{
            return traversePathImpl(path, filter);
        } catch(IOException e){
            TraverserLogger.warning(getClass(), "some error", e);
        }
        return Stream.empty();
    }

    private Stream<Path> traversePathImpl(Path path, Filter filter) throws IOException{
        if(isDirectory(path))
            return directoryStream(path, filter);
        return Stream.of(path);
    }

    private Stream<Path> directoryStream(Path path, Filter filter) throws IOException{
        DirectoryStream<Path> stream = provider.newDirectoryStream(path, filter.toFilter());
        return StreamSupport.stream(stream.spliterator(), false)
                .flatMap(target -> traversePath(target, filter));
    }

    private boolean isDirectory(Path path) throws IOException{
        BasicFileAttributes attributes = provider
                .readAttributes(path, BasicFileAttributes.class);
        return attributes.isDirectory();
    }
}
