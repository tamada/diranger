package jp.cafebabe.dwalker.ignorefiles;

import java.io.IOException;
import java.nio.file.Path;

public class GitIgnore implements IgnoreFile {
    @Override
    public boolean isIgnore(Path path) {
        System.out.printf("GitIgnore.isIgnore(%s)%n", path);
        return false;
    }

    public static class Builder implements IgnoreFile.Builder {
        @Override
        public IgnoreFile build(Path path) throws IOException {
            System.out.printf("GitIgnore.Builder.build(%s)%n", path);
            return null;
        }
    }
}
