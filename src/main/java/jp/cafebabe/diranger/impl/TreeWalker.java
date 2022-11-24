package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import jp.cafebabe.diranger.Entry;
import jp.cafebabe.diranger.ignorefiles.GitIgnoreVisitor;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.util.function.BiConsumer;

public class TreeWalker {
    private final Config config;
    private final Entry base;

    private boolean topDirectoryCheckDone = false;

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
            callFailure((entry, ee) -> visitor.postVisitDirectory(entry, ee),
                    dir, exc, "postVisitDirectory");
        }
    }

    private void callFailure(ThrowableBiConsumer<Entry, IOException, IOException> consumer,
                             Entry e, IOException exc, String label) {
        try {
            consumer.accept(e, exc);
        } catch(IOException ee) {
            LoggerFactory.getLogger(getClass())
                    .warn("fail " + label, ee);
        }
    }

    private void visitEntry(Entry entry, FileVisitor<Entry> visitor) {
        if(!entry.isDirectory()) {
            try {
                visitor.visitFile(entry, entry.attributes());
            } catch(IOException e) {
                callFailure((entry2, exc) -> visitor.visitFileFailed(entry2, exc),
                        entry, e, "visitFileFailed");
            }
        }
        else
            visitDirectory(entry, visitor);
    }
}
