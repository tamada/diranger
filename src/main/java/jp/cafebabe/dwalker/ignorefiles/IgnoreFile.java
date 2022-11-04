package jp.cafebabe.dwalker.ignorefiles;

import java.io.IOException;
import java.nio.file.Path;

public interface IgnoreFile {
    /**
     *
     * @param path
     * @return
     */
    boolean isIgnore(Path path);

    interface Builder {
        /**
         * Build an instance of IgnoreFile from the given ignore file.
         * @param path a path of an ignore file.
         * @return constructed ignore file.
         * @throws IOException I/O error
         */
        IgnoreFile build(Path path) throws IOException;
    }
}
