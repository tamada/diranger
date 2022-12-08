package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;
import org.eclipse.jgit.ignore.IgnoreNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class GitIgnoreFile implements IgnoreFile {
    private final IgnoreNode node;
    private final Entry base;
    private final IgnoreFile parent;

    private GitIgnoreFile(Entry base, IgnoreNode node, IgnoreFile parent) {
        this.base = base;
        this.node = node;
        this.parent = parent;
    }

    public IgnoreFile parent() {
        return parent;
    }

    public Entry base() {
        return base;
    }

    @Override
    public Boolean checkIgnore(Entry path) {
        Path relative = base.relativize(path);
        var result = node.checkIgnored(relative.toString(), path.isDirectory());
        return result;
    }

    public static class Builder implements IgnoreFile.Builder {
        @Override
        public IgnoreFile build(Entry ignoreFile, IgnoreFile parent) throws IOException {
            if(!FS.exists(ignoreFile))
                return parent;
            IgnoreNode node = new IgnoreNode();
            try(InputStream in = ignoreFile.provider().newInputStream(ignoreFile.path())) {
                node.parse(in);
            }
            System.out.printf("GitIgnoreFile(%s): parent: %s%n", ignoreFile, ignoreFile.parent());
            return new GitIgnoreFile(ignoreFile.parent(), node, parent);
        }
    }
}
