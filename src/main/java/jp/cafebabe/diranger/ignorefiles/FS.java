package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;

public class FS {
    public static boolean exists(Entry entry, LinkOption... options) {
        return exists(entry.path(), entry.provider(), options);
    }

    /**
     * The routine of this method is copied from java.nio.file.Files.
     */
    public static boolean exists(Path path, FileSystemProvider provider, LinkOption... options) {
        try {
            if (followLinks(options)) {
                provider.checkAccess(path);
            } else {
                // attempt to read attributes without following links
                provider.readAttributes(path, BasicFileAttributes.class,
                        LinkOption.NOFOLLOW_LINKS);
            }
            // file exists
            return true;
        } catch (IOException x) {
            // does not exist or unable to determine if file exists
            return false;
        }
    }

    /**
     * The routine of this method is copied from java.nio.file.Files.
     */
    private static boolean followLinks(LinkOption... options) {
        boolean followLinks = true;
        for (LinkOption opt: options) {
            if (opt == LinkOption.NOFOLLOW_LINKS) {
                followLinks = false;
                continue;
            }
            if (opt == null)
                throw new NullPointerException();
            throw new AssertionError("Should not get here");
        }
        return followLinks;
    }
}
