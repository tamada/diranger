package jp.cafebabe.diranger;

import jp.cafebabe.diranger.impl.EmptyIterator;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The traverser class for the directories.
 * @author Haruaki TAMADA
 */
public interface Ranger {
    Stream<Entry> stream(Entry base, Config config) throws IOException;

    default Stream<Entry> stream(Path base, Config config) throws IOException {
        return stream(Entry.of(base), config);
    }

    default Iterator<Entry> iterator(Entry base, Config config) throws IOException {
        return stream(base, config)
                .iterator();
    }

    default Iterator<Entry> iterator(Path base, Config config) throws IOException {
        return stream(base, config)
                .iterator();
    }

    /**
     * <pre> return () -> iterator(base, config);</pre>
     * @param base the base directory for traversing.
     * @param config configuration.
     * @return an <code>Iterable</code> object.
     * In the case of some error, error messages send to slf4j, and returns <code>Iterable</code> object with empty iterator.
     */
    default Iterable<Entry> iterable(Path base, Config config) {
        return () -> {
            try {
                return iterator(base, config);
            } catch(IOException e) {
                LoggerFactory.getLogger(getClass())
                        .warn("i/o error", e);
            }
            return new EmptyIterator<>();
        };
    }

    /**
     * <code>return stream(base, config).collect(Collectors.toList());</code>
     * @param base the base directory for traversing.
     * @param config configration
     * @return file list under the base directory.
     * @throws IOException I/O error
     */
    default List<Entry> list(Entry base, Config config) throws IOException {
        return stream(base, config)
                .collect(Collectors.toList());
    }
}
