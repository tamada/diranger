package jp.cafebabe.diranger;

import com.github.traverser.Filter;
import com.github.traverser.TraverserLogger;
import jp.cafebabe.diranger.ignorefiles.IgnoreManager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DefaultWalker implements Walker {
    private final Path basePath;
    private final Config config;
    private final FileSystemProvider provider;
    private final DirectoryStream.Filter<Path> filter;

    public DefaultWalker(FileSystem fs, Path base, Config config) {
        this.basePath = base;
        this.provider = fs.provider();
        this.config = config;
        this.filter = new WalkerFilter(config, provider);
    }

    public Iterator<Path> iterator() {
        List<Entry> list = new ArrayList<>();
        IgnoreManager manager = IgnoreManager.of(config.respectIgnoreFiles(), provider);
        traverse(basePath, list, manager);
        return list.stream().map(Entry::path).iterator();
    }
    public Stream<Path> stream() {
        var spliterator = Spliterators.spliteratorUnknownSize(iterator(), 0);
        return StreamSupport.stream(spliterator, false);
    }

    private void traverse(Path base, List<Entry> list, IgnoreManager manager) {
        try {
            traverseImpl(base, list, manager);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void traverse(Entry base, List<Entry> list, IgnoreManager manager) {
        try {
            traverseImpl(base, list, manager);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void traverseImpl(Path base, List<Entry> list, IgnoreManager manager) throws IOException {
        traverseImpl(new Entry(base, provider), list, manager);
    }

    private void traverseImpl(Entry entry, List<Entry> list, IgnoreManager manager) throws IOException {
        if(!config.isTarget(entry))
            return;
        if(!entry.isDirectory()) {
            if (!manager.isIgnore(entry))
                list.add(entry);
            return;
        } // else directory
        if(!manager.isIgnore(entry)) {
            var newManager = manager.visitDirectory(entry);
            entry.list(config)
                    .filter(e -> newManager.isIgnore(e))
                    .forEach(e -> traverse(e, list, newManager));
        }
    }

    private Stream<Path> traversePath(Path path, Filter filter){
        try{
            return traversePathImpl(path, filter);
        } catch(IOException e){
            TraverserLogger.warning(getClass(), "some error", e);
        }
        return Stream.empty();
    }

    private Stream<Path> traversePathImpl(Path path, Filter filter) throws IOException{
        if(isDirectory(path))
            return directoryStream(path, filter);
        return Stream.of(path);
    }

    private Stream<Path> directoryStream(Path path, Filter filter) throws IOException{
        DirectoryStream<Path> stream = provider.newDirectoryStream(path, filter.toFilter());
        return StreamSupport.stream(stream.spliterator(), false)
                .flatMap(target -> traversePath(target, filter));
    }

    private boolean isDirectory(Path path) throws IOException{
        BasicFileAttributes attributes = provider
                .readAttributes(path, BasicFileAttributes.class);
        return attributes.isDirectory();
    }
}
