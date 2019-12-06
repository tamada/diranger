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
        return stream(Traverser.map(streamFilter));
    }

    default Stream<FileItem> stream(java.io.FileFilter fileFilter) throws IOException{
        return stream(Traverser.map(fileFilter));
    }

    default Stream<FileItem> stream(FileFilter fileFilter) throws IOException{
        return stream(Traverser.map(fileFilter));
    }

    default Stream<FileItem> stream() throws IOException{
        return stream((Filter)(path -> true));
    }
}
