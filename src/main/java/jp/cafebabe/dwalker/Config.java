package jp.cafebabe.dwalker;

import java.util.BitSet;

public class Config {
    private static final int RESPECT_IGNORE_FILES = 1;
    private static final int SKIP_HIDDEN_FILES = 1;
    private static final int FOLLOW_SYMLINKS = 1;

    private BitSet bits = new BitSet();

    private Config(boolean respectIgnoreFiles, boolean followSymlinks,
                  boolean skipHiddenFiles) {
        setBit(respectIgnoreFiles, RESPECT_IGNORE_FILES);
        setBit(followSymlinks, FOLLOW_SYMLINKS);
        setBit(skipHiddenFiles, SKIP_HIDDEN_FILES);
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

    public boolean followSymlinks() {
        return bits.get(FOLLOW_SYMLINKS);
    }

    public boolean skipHiddenFiles() {
        return bits.get(SKIP_HIDDEN_FILES);
    }

    static final class Builder {
        private boolean respect;
        private boolean follow;
        private boolean skip;

        public void followSymlinks(boolean flag) {
            this.follow = flag;
        }

        public void respectIgnoreFiles(boolean flag) {
            this.respect = flag;
        }

        public void skipHiddenFiles(boolean flag) {
            this.skip = flag;
        }

        public Config build() {
            return new Config(respect, follow, skip);
        }
    }
}
