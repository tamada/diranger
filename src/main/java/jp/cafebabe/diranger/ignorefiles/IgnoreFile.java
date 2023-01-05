package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;

import java.io.IOException;
import java.util.Optional;

public interface IgnoreFile {
    Boolean checkIgnore(Entry path);

    default boolean isIgnore(Entry path) {
        Boolean result = checkIgnore(path);
        if (result == null)
            return Optional.ofNullable(parent())
                    .map(p -> p.isIgnore(path))
                    .orElse(false);
        return result;
    }

    Entry base();

    IgnoreFile parent();

    final class Null implements IgnoreFile {
        @Override
        public Boolean checkIgnore(Entry path) {
            return false;
        }

        @Override
        public Entry base() {
            return null;
        }

        @Override
        public IgnoreFile parent() {
            return null;
        }
    }

    interface Builder {
        /**
         * Build instance list of IgnoreFile from the given directory.
         * @param ignoreFile a directory path.
         * @return a stream of constructed ignore files.
         *   If the ignore files are not exists, this method returns empty stream.
         * @throws IOException I/O error
         */
        default IgnoreFile build(Entry ignoreFile) throws IOException {
            return build(ignoreFile, null);
        }

        IgnoreFile build(Entry ignoreFile, IgnoreFile parent) throws IOException;
    }
}
