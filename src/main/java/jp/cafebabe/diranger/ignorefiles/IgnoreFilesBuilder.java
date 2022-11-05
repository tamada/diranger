package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;
import jp.cafebabe.diranger.FS;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.stream.Stream;

public class IgnoreFilesBuilder implements IgnoreFile.Builder {
    private static final Map<Path, IgnoreFile.Builder> mapper = Map.of(Path.of(".gitignore"), new GitIgnoreFile.Builder());

    private final FileSystemProvider provider;

    public IgnoreFilesBuilder(FileSystemProvider provider) {
        this.provider = provider;
    }

    @Override
    public Stream<IgnoreFile> build(Entry entry) throws IOException {
        Path path = entry.path();
        return mapper.keySet().stream()
                .map(p -> path.resolve(p))
                .filter(p -> FS.exists(p, provider))
                .flatMap(p -> buildEach(new Entry(p, provider)));
    }

    private Stream<IgnoreFile> buildEach(Entry path) {
        Path sub = path.path().getFileName();
        var builder = mapper.get(sub);
        try {
            return builder.build(path);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }
}
