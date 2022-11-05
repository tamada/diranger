package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;

import java.io.IOException;
import java.nio.file.spi.FileSystemProvider;
import java.util.stream.Collectors;

public interface IgnoreManager extends IgnoreFile {
    static IgnoreManager of(boolean respectIgnore, FileSystemProvider provider) {
        if(!respectIgnore)
            return new Null();
        return new Default(new IgnoreFilesBuilder(provider));
    }

    /**
     * This method reads ignore files in the given directory.
     * Examples of the ignore files are: <code>.gitignore</code>, <code>.dockerignore</code>, etc.
     * @param path a directory
     * @return an instance of IgnoreManager for the directory.
     */
    IgnoreManager visitDirectory(Entry path) throws IOException;

    final class Default implements IgnoreManager {
        private IgnoreManager parent;
        private IgnoreFile current;
        private final IgnoreFilesBuilder builder;

        private Default(IgnoreFilesBuilder builder) {
            this.builder = builder;
        }

        private Default(IgnoreManager parent, IgnoreFile current, IgnoreFilesBuilder builder) {
            this(builder);
            this.parent = parent;
            this.current = current;
        }

        @Override
        public IgnoreManager visitDirectory(Entry path) throws IOException {
            System.out.printf("visitDirectory(%s)%n", path);
            var ifs = builder.build(path)
                    .collect(Collectors.toList());
            if(ifs.size() == 0)
                return this;
            return new Default(this, new IgnoreFiles(ifs.stream()), builder);
        }

        @Override
        public boolean isIgnore(Entry path) {
            boolean result = current != null && current.isIgnore(path);
            if(!result && parent != null)
                return parent.isIgnore(path);
            return result;
        }
    }

    final class Null implements IgnoreManager {
        @Override
        public boolean isIgnore(Entry path) {
            return false;
        }

        @Override
        public IgnoreManager visitDirectory(Entry path) {
            return this;
        }
    }
}
