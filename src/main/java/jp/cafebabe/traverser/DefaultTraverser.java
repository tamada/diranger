package jp.cafebabe.traverser;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class DefaultTraverser extends AbstractTraverser{
    private FileSystemProvider provider;
    private Path entryPoint;

    DefaultTraverser(FileSystem system, Path entryPoint, Path basePath){
        super(basePath);
        this.provider = system.provider();
        this.entryPoint = entryPoint;
    }

    public BasicFileAttributes attributes(Path path) throws IOException {
        return provider.readAttributes(path, BasicFileAttributes.class);
    }

    @Override
    public Stream<Path> stream(Filter filter) throws IOException {
        return traversePathImpl(entryPoint, filter);
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
        return attributes(path).isDirectory();
    }
}
