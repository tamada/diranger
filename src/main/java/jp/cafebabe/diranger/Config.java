package jp.cafebabe.diranger;

import jp.cafebabe.diranger.impl.WalkerFilter;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.BitSet;

/**
 * A class for configuration of directory traverser.
 * The object from this class has three parameters.
 * <dl>
 *     <dt><code>respectIgnoreFiles</code></dt>
 *     <dd>If this parameter is <code>true</code>, the traverser reads <code>.gitignore</code> file if it exists.</dd>
 *     <dt><code>skipHiddenFiles</code></dt>
 *     <dd>If this parameter is <code>true</code>, the traverser skip hidden files.</dd>
 *     <dt><code>skipSymlinks</code></dt>
 *     <dd>
 *         If this parameter is <code>true</code>, the traverser skip symbolic links.
 *         If the directory layout is recursively, the traverser throw an <code>IOException</code>.
 *     </dd>
 * </dl>
 * @author Haruaki TAMADA
 */
public class Config {
    private static final int RESPECT_IGNORE_FILES = 1;
    private static final int SKIP_HIDDEN_FILES = 2;
    private static final int SKIP_SYMLINKS = 3;

    private final BitSet bits = new BitSet();

    /**
     * The instance from this constructor skips the hidden files, and symbolic links,
     * and respects git ignore files.
     * To set the parameter of the instance, use {@link Builder <code>Builder</code>} instance.
     */
    public Config() {
        this(true, true, true);
    }

    private Config(boolean respectIgnoreFiles, boolean skipSymlinks,
                  boolean skipHiddenFiles) {
        setBit(respectIgnoreFiles, RESPECT_IGNORE_FILES);
        setBit(skipSymlinks, SKIP_SYMLINKS);
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

    public boolean skipSymlinks() {
        return bits.get(SKIP_SYMLINKS);
    }

    public boolean skipHiddenFiles() {
        return bits.get(SKIP_HIDDEN_FILES);
    }

    DirectoryStream.Filter<Path> buildFilter(FileSystemProvider provider) {
        return new WalkerFilter(this, provider);
    }

    /**
     * Builder class for <code>{@link Config Config}</code> class.
     */
    public static final class Builder {
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
