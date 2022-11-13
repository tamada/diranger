package jp.cafebabe.diranger;

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
        System.out.printf("%s: hidden:  config: %s, file: %s%n", path, config.skipHiddenFiles(), provider.isHidden(path));
        System.out.printf("%s: symlink: config: %s, file: %s%n", path, config.skipSymlinks(), isSymbolicLink(path));
        if(config.skipHiddenFiles() && provider.isHidden(path))
            return false;
        return !config.skipSymlinks() || !isSymbolicLink(path);
    }

    private boolean isSymbolicLink(Path path) {
        try {
            return provider.readAttributes(path, BasicFileAttributes.class,
                            LinkOption.NOFOLLOW_LINKS)
                    .isSymbolicLink();
        } catch(IOException e) {
            return false;
        }
    }
}
