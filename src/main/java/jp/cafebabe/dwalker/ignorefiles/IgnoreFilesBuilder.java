package jp.cafebabe.dwalker.ignorefiles;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;

public class IgnoreFilesBuilder implements IgnoreFile.Builder {
    private static final Map<Path, IgnoreFile.Builder> mapper = Map.of(Path.of(".gitignore"), new GitIgnore.Builder());

    private final FileSystemProvider provider;

    public IgnoreFilesBuilder(FileSystemProvider provider) {
        this.provider = provider;
    }

    @Override
    public IgnoreFile build(Path path) throws IOException {
        return null;
    }
}
