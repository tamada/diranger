package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class GitIgnoreVisitor implements FileVisitor<Entry> {
    private final FileVisitor<Entry> delegate;
    private IgnoreFile current;

    public GitIgnoreVisitor(FileVisitor<Entry> delegate) {
        this.delegate = delegate;
        current = new IgnoreFile.Null();
    }

    @Override
    public FileVisitResult preVisitDirectory(Entry dir, BasicFileAttributes attrs) throws IOException {
        Entry ignoreFile = dir.resolve(".gitignore");
        if(FS.exists(ignoreFile)) {
            current = new GitIgnoreFile.Builder().build(ignoreFile, current);
        }
        if(current == null || current.parent() == null || !current.parent().isIgnore(dir))
            delegate.preVisitDirectory(dir, attrs);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Entry file, BasicFileAttributes attrs) throws IOException {
        if(current != null && !current.isIgnore(file))
            delegate.visitFile(file, attrs);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Entry file, IOException exc) throws IOException {
        if(!current.isIgnore(file))
            delegate.visitFileFailed(file, exc);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Entry dir, IOException exc) throws IOException {
        if(current != null) {
            Entry base = current.base();
            if (base != null && base.isSame(dir.path()))
                current = current.parent();
        }
        if(current != null && !current.isIgnore(dir))
            delegate.postVisitDirectory(dir, exc);
        return FileVisitResult.CONTINUE;
    }
}
