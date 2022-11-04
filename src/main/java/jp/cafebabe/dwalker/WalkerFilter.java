package jp.cafebabe.dwalker;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;

public class WalkerFilter implements DirectoryStream.Filter<Path> {
    private final FileSystemProvider provider;
    private final Config config;

    public WalkerFilter(Config config, FileSystemProvider provider) {
        this.config = config;
        this.provider = provider;
    }

    @Override
    public boolean accept(Path path) throws IOException {
        var attributes = provider.readAttributes(path, BasicFileAttributes.class);
        if(config.skipHiddenFiles() && provider.isHidden(path))
            return false;
        return !config.skipSymlinks() || !attributes.isSymbolicLink();
    }
}
