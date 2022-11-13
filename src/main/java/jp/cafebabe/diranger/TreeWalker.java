package jp.cafebabe.diranger;

import jp.cafebabe.diranger.ignorefiles.GitIgnoreVisitor;

import java.io.IOException;
import java.nio.file.FileVisitor;

public class TreeWalker {
    private final Config config;
    private final Entry base;

    public TreeWalker(Config config, Entry base) {
        this.config = config;
        this.base = base;
    }

    private FileVisitor<Entry> wrapVisitorIfNeeded(FileVisitor<Entry> visitor) {
        if(!config.respectIgnoreFiles())
            return visitor;
        return new GitIgnoreVisitor(visitor);
    }

    public void accept(FileVisitor<Entry> visitor) throws IOException {
        FileVisitor<Entry> newVisitor = wrapVisitorIfNeeded(visitor);
        visitDirectory(base, newVisitor);
    }

    private void visitEntries(Entry dir, FileVisitor<Entry> visitor) throws IOException {
        dir.list(config)
                .forEach(e -> visitEntry(e, visitor));
    }

    private void visitDirectory(Entry dir, FileVisitor<Entry> visitor) {
        IOException exc = null;
        try{
            visitor.preVisitDirectory(dir, dir.attributes());
            visitEntries(dir, visitor);
        } catch(IOException e) {
            exc = e;
        } finally {
            try {
                visitor.postVisitDirectory(dir, exc);
            } catch(IOException ee) {
                ee.printStackTrace();
            }
        }
    }

    private void visitEntry(Entry entry, FileVisitor<Entry> visitor) {
        if(!entry.isDirectory()) {
            try {
                visitor.visitFile(entry, entry.attributes());
            } catch(IOException e) {
                try {
                    visitor.visitFileFailed(entry, e);
                } catch(IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
        else
            visitDirectory(entry, visitor);
    }
}
