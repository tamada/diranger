package com.github.traverser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

class DefaultTraverser2 implements Traverser2 {
    private DefaultTraverser traverser;

    public DefaultTraverser2(DefaultTraverser traverser) {
        this.traverser = traverser;
    }

    @Override
    public Stream<FileItem> stream(Filter filter) throws IOException {
        return traverser.stream(filter)
                .map(path -> toFileItem(path));
    }

    private FileItem toFileItem(Path path) {
        return FileItem.build(path, traverser);
    }

    @Override
    public Path basePath() {
        return traverser.basePath();
    }
    
    
}
