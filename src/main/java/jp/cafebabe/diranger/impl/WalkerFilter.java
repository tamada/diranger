package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.LinkOption;
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
        return !isSkipHiddenFile(path) && !isSkipSymbolicLinks(path);
    }

    private boolean isSkipHiddenFile(Path path) throws IOException {
        return config.skipHiddenFiles() && provider.isHidden(path);
    }

    private boolean isSkipSymbolicLinks(Path path) {
        return config.skipSymlinks() && isSymbolicLink(path);
    }

    private boolean isSymbolicLink(Path path) {
        try {
            return provider.readAttributes(path, BasicFileAttributes.class,
                            LinkOption.NOFOLLOW_LINKS)
                    .isSymbolicLink();
        } catch(IOException e) {
            LoggerFactory.getLogger(getClass()).warn("fail readAttributes", e);
            return false;
        }
    }
}
