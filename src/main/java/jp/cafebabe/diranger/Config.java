package jp.cafebabe.diranger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.BitSet;
import java.util.Iterator;

public class Config {
    private static final int RESPECT_IGNORE_FILES = 1;
    private static final int SKIP_HIDDEN_FILES = 2;
    private static final int SKIP_SYMLINKS = 3;

    private final BitSet bits = new BitSet();

    private Config(boolean respectIgnoreFiles, boolean skipSymlinks,
                  boolean skipHiddenFiles) {
        setBit(respectIgnoreFiles, RESPECT_IGNORE_FILES);
        setBit(skipSymlinks, SKIP_SYMLINKS);
        setBit(skipHiddenFiles, SKIP_HIDDEN_FILES);
    }

    public boolean isTarget(Entry entry) {
        if(!FS.exists(entry))
            return false;
        if(skipHiddenFiles() && entry.isHidden())
            return false;
        return !skipSymlinks() || !entry.isSymlink();
    }

    private void setBit(boolean flag, int index) {
        if(flag)
            bits.set(index);
        else
            bits.clear(index);
    }

    public boolean respectIgnoreFiles() {
        return bits.get(RESPECT_IGNORE_FILES);
    }

    public boolean skipSymlinks() {
        return bits.get(SKIP_SYMLINKS);
    }

    public boolean skipHiddenFiles() {
        return bits.get(SKIP_HIDDEN_FILES);
    }

    public DirectoryStream.Filter<Path> buildFilter(FileSystemProvider provider) {
        return new WalkerFilter(this, provider);
    }

    static final class Builder {
        private boolean respect = true;
        private boolean symlink = true;
        private boolean hidden = true;

        public Builder skipSymlinks(boolean flag) {
            this.symlink = flag;
            return this;
        }

        public Builder respectIgnoreFiles(boolean flag) {
            this.respect = flag;
            return this;
        }

        public Builder skipHiddenFiles(boolean flag) {
            this.hidden = flag;
            return this;
        }

        public Config build() {
            return new Config(respect, symlink, hidden);
        }
    }
}
