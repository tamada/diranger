package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import jp.cafebabe.diranger.Entry;
import jp.cafebabe.diranger.ignorefiles.GitIgnoreVisitor;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;

public class TreeWalker {
    private final Config config;
    private final Entry base;

    public TreeWalker(Entry base, Config config) {
        this.config = config;
        this.base = base;
    }

    private FileVisitor<Entry> wrapVisitorForGitIgnore(FileVisitor<Entry> visitor) {
        if(!config.respectIgnoreFiles())
            return visitor;
        return new GitIgnoreVisitor(visitor);
    }

    public void accept(FileVisitor<Entry> visitor) throws IOException {
        FileVisitor<Entry> newVisitor = wrapVisitorForGitIgnore(visitor);
        visitDirectory(base, newVisitor);
    }

    private void visitDirectory(Entry dir, FileVisitor<Entry> visitor) {
        IOException exc = null;
        try{
            if(visitor.preVisitDirectory(dir, dir.attributes()) == FileVisitResult.CONTINUE)
                visitEntries(dir, visitor);
        } catch(IOException e) {
            exc = e;
        } finally {
            callFailure((entry, ee) -> visitor.postVisitDirectory(entry, ee),
                    dir, exc, "postVisitDirectory");
        }
    }

    private void callFailure(ThrowableBiConsumer<Entry, IOException, IOException> consumer,
                             Entry entry, IOException exc, String label) {
        try {
            consumer.accept(entry, exc);
        } catch(IOException ee) {
            LoggerFactory.getLogger(getClass())
                    .warn("fail " + label, ee);
        }
    }

    private void visitEntries(Entry dir, FileVisitor<Entry> visitor) throws IOException {
        dir.list(config)
                .forEach(entry -> visitEntry(entry, visitor));
    }

    private void visitEntry(Entry entry, FileVisitor<Entry> visitor) {
        if(!entry.isDirectory()) {
            try {
                visitor.visitFile(entry, entry.attributes());
            } catch(IOException e) {
                callFailure(visitor::visitFileFailed,
                        entry, e, "visitFileFailed");
            }
        }
        else
            visitDirectory(entry, visitor);
    }
}
