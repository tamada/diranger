package jp.cafebabe.dwalker;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class WalkerBuilder {
    private final Path basePath;
    private final Config.Builder builder = new Config.Builder();

    public WalkerBuilder(Path basePath) {
        this.basePath = basePath;
    }

    public void skipHiddenFiles(boolean flag) {
        builder.skipHiddenFiles(flag);
    }

    public void skipSymlinks(boolean flag) {
        builder.skipSymlinks(flag);
    }

    public void respectIgnore(boolean flag) {
        builder.respectIgnoreFiles(flag);
    }

    public Walker build() {
        try {
            return buildImpl(builder.build());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Walker buildImpl(Config config) throws IOException {
        if(isJarOrZipFile(basePath))
            return createJarWalker(basePath, config);
        return createDefaultWalker(basePath, config);
    }

    private Walker createJarWalker(Path path, Config config) throws IOException {
        FileSystem fs = FileSystems
                .newFileSystem(path, getClass().getClassLoader());
        return new DefaultWalker(fs, fs.getPath("/"), config);
    }

    private Walker createDefaultWalker(Path path, Config config) throws IOException{
        FileSystem fs = FileSystems.getDefault();
        return new DefaultWalker(fs, path, config);
    }

    private boolean isJarOrZipFile(Path path){
        String name = path.toString();
        return name.endsWith(".jar")
                || name.endsWith(".zip");
    }
}
