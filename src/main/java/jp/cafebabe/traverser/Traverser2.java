package jp.cafebabe.traverser;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.swing.filechooser.FileFilter;

public interface Traverser2 {
    Stream<FileItem> stream(Filter filter) throws IOException;

    Path basePath();

    default Stream<FileItem> stream(DirectoryStream.Filter<Path> streamFilter) throws IOException{
        Filter filter = path -> streamFilter.accept(path);
        return stream(filter);
    }

    default Stream<FileItem> stream(java.io.FileFilter fileFilter) throws IOException{
        Filter filter = path -> fileFilter.accept(path.toFile());
        return stream(filter);
    }

    default Stream<FileItem> stream(FileFilter fileFilter) throws IOException{
        Filter filter = path -> fileFilter.accept(path.toFile());
        return stream(filter);
    }

    default Stream<FileItem> stream() throws IOException{
        Filter filter = path -> true;
        return stream(filter);
    }
}
