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
 * The main interface for traversing the target directory.
 * @author Haruaki TAMADA
 */
public interface Ranger {
    /**
     * return a stream object contains all entries under the <code>base</code> directory
     * excepting the files and directories followed by <code>config</code> object.
     * @param base   the base directory for traversing.
     * @param config configuration.
     * @return a stream object of the entries under the <code>base</code> directory.
     * @throws IOException I/O error
     */
    Stream<Entry> stream(Entry base, Config config) throws IOException;

    /**
     * <code>return stream(Entry.of(base), config);</code>
     * @param base the base directory for traversing.
     * @param config configuration.
     * @return an <code>Iterator</code> object.
     * @throws IOException I/O error
     * @see Entry#of
     */
    default Stream<Entry> stream(Path base, Config config) throws IOException {
        return stream(Entry.of(base), config);
    }

    /**
     * <code>return stream(base, config).iterator();</code>
     * @param base the base directory for traversing.
     * @param config configuration.
     * @return an <code>Iterator</code> object.
     * @throws IOException I/O error
     */
    default Iterator<Entry> iterator(Entry base, Config config) throws IOException {
        return stream(base, config)
                .iterator();
    }

    /**
     * <code>return stream(base, config).iterator();</code>
     * @param base the base directory for traversing.
     * @param config configuration.
     * @return an <code>Iterator</code> object.
     * @throws IOException I/O error
     */
    default Iterator<Entry> iterator(Path base, Config config) throws IOException {
        return stream(base, config)
                .iterator();
    }

    /**
     * <pre> return () -> iterator(base, config);</pre>
     * @param base the base directory for traversing.
     * @param config configuration.
     * @return an <code>Iterable</code> object.
     * In the case of some error, error messages send to slf4j,
     * and this method returns an empty <code>Iterable</code> object.
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
