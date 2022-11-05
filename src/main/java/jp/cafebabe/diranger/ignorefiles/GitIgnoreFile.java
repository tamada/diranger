package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;
import org.eclipse.jgit.ignore.IgnoreNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

public class GitIgnoreFile implements IgnoreFile {
    private IgnoreNode node;
    private Entry base;

    private GitIgnoreFile(Entry base, IgnoreNode node) {
        this.base = base;
        this.node = node;
    }

    @Override
    public boolean isIgnore(Entry path) {
        System.out.printf("GitIgnore.isIgnore(%s)%n", path);
        Path relative = base.path().resolve(path.path());
        return node.checkIgnored(relative.toString(), path.isDirectory());
    }

    public static class Builder implements IgnoreFile.Builder {
        @Override
        public Stream<IgnoreFile> build(Entry path) throws IOException {
            System.out.printf("GitIgnore.Builder.build(%s)%n", path);
            IgnoreNode node = new IgnoreNode();
            try(InputStream in = path.provider().newInputStream(path.path())) {
                node.parse(in);
            }
            return Stream.of(new GitIgnoreFile(path, node));
        }
    }
}
