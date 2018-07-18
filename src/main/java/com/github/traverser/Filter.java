package com.github.traverser;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

public interface Filter {
    boolean accept(Path path) throws IOException;

    default DirectoryStream.Filter<Path> toFilter(){
        DirectoryStream.Filter<Path> filter = path -> this.accept(path);
        return filter;
    }
}
