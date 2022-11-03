package jp.cafebabe.dwalker;

import com.github.traverser.DefaultTraverser;
import com.github.traverser.Traverser;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class WalkerBuilder {
    private Path basePath;
    private Config.Builder builder = new Config.Builder();

    public WalkerBuilder(Path basePath) {
        this.basePath = basePath;
    }

    public void skipHiddenFiles(boolean flag) {
        builder.skipHiddenFiles(flag);
    }

    public void followSymlinks(boolean flag) {
        builder.followSymlinks(flag);
    }

    public void respectIgnore(boolean flag) {
        builder.respectIgnoreFiles(flag);
    }

    public Walker build() throws IOException {
        if(isJarOrZipFile(basePath))
            return createJarWalker(basePath);
        return createDefaultWalker(basePath);
    }

    private Traverser createJarTraverser(Path path) throws IOException {
        FileSystem fs = FileSystems
                .newFileSystem(path, getClass().getClassLoader());
        return new DefaultWalker(fs, fs.getPath("/"), path, builder.build());
    }

    private Traverser createDefaultTraverser(Path path) throws IOException{
        FileSystem fs = FileSystems.getDefault();
        return new DefaultWalker(fs, path, path, builder.build());
    }

    private boolean isJarOrZipFile(Path path){
        String name = path.toString();
        return name.endsWith(".jar")
                || name.endsWith(".zip");
    }
}
