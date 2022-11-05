package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface IgnoreFile {
    /**
     *
     * @param path
     * @return
     */
    boolean isIgnore(Entry path);

    interface Builder {
        /**
         * Build instance list of IgnoreFile from the given directory.
         * @param directory a directory path.
         * @return a stream of constructed ignore files.
         *   If the ignore files are not exists, this method returns empty stream.
         * @throws IOException I/O error
         */
        Stream<IgnoreFile> build(Entry directory) throws IOException;
    }
}
