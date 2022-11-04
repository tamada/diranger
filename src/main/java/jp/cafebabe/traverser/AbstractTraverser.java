package jp.cafebabe.traverser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public abstract class AbstractTraverser implements Traverser{
    private final Path basePath;

    public AbstractTraverser(Path basePath){
        this.basePath = basePath;
    }

    @Override
    public abstract Stream<Path> stream(Filter filter) throws IOException;

    @Override
    public Path basePath() {
        return basePath;
    }
}
