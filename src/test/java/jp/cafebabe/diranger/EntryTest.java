package jp.cafebabe.diranger;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntryTest {
    @Test
    public void testListDefault() throws IOException {
        Config config = new Config.Builder()
                .build();
        Entry entry = new Entry(Path.of("src/test/resources/samples"));
        var list = entry.list(config).sorted().collect(Collectors.toList());
        assertEquals(list.size(), 6);
        assertEquals(list.get(0).toString(), "src/test/resources/samples/dir1");
        assertEquals(list.get(1).toString(), "src/test/resources/samples/dir2");
        assertEquals(list.get(2).toString(), "src/test/resources/samples/dir3");
        assertEquals(list.get(3).toString(), "src/test/resources/samples/file1");
        assertEquals(list.get(4).toString(), "src/test/resources/samples/file2");
        assertEquals(list.get(5).toString(), "src/test/resources/samples/file3");
    }

    @Test
    public void testListAll() throws IOException {
        Config config = new Config.Builder().skipSymlinks(false)
                .skipHiddenFiles(false)
                .respectIgnoreFiles(false)
                .build();
        Entry entry = new Entry(Path.of("src/test/resources/samples"));
        var list = entry.list(config).sorted().collect(Collectors.toList());
        assertEquals(list.size(), 10);
        assertEquals(list.get(0).toString(), "src/test/resources/samples/.hidden_dir1");
        assertEquals(list.get(1).toString(), "src/test/resources/samples/.hidden_file1");
        assertEquals(list.get(2).toString(), "src/test/resources/samples/.hidden_symlink1");
        assertEquals(list.get(3).toString(), "src/test/resources/samples/dir1");
        assertEquals(list.get(4).toString(), "src/test/resources/samples/dir2");
        assertEquals(list.get(5).toString(), "src/test/resources/samples/dir3");
        assertEquals(list.get(6).toString(), "src/test/resources/samples/file1");
        assertEquals(list.get(7).toString(), "src/test/resources/samples/file2");
        assertEquals(list.get(8).toString(), "src/test/resources/samples/file3");
        assertEquals(list.get(9).toString(), "src/test/resources/samples/symlink1");
    }

    @Test
    public void testListSkipSymlink() throws IOException {
        Config config = new Config.Builder().skipSymlinks(true)
                .skipHiddenFiles(false)
                .respectIgnoreFiles(false)
                .build();
        Entry entry = new Entry(Path.of("src/test/resources/samples"));
        var list = entry.list(config).sorted().collect(Collectors.toList());
        assertEquals(list.size(), 8);
        assertEquals(list.get(0).toString(), "src/test/resources/samples/.hidden_dir1");
        assertEquals(list.get(1).toString(), "src/test/resources/samples/.hidden_file1");
        assertEquals(list.get(2).toString(), "src/test/resources/samples/dir1");
        assertEquals(list.get(3).toString(), "src/test/resources/samples/dir2");
        assertEquals(list.get(4).toString(), "src/test/resources/samples/dir3");
        assertEquals(list.get(5).toString(), "src/test/resources/samples/file1");
        assertEquals(list.get(6).toString(), "src/test/resources/samples/file2");
        assertEquals(list.get(7).toString(), "src/test/resources/samples/file3");
    }

    @Test
    public void testListSkipHidden() throws IOException {
        Config config = new Config.Builder().skipSymlinks(false)
                .skipHiddenFiles(true)
                .respectIgnoreFiles(false)
                .build();
        Entry entry = new Entry(Path.of("src/test/resources/samples"));
        var list = entry.list(config).sorted().collect(Collectors.toList());
        assertEquals(list.size(), 7);
        assertEquals(list.get(0).toString(), "src/test/resources/samples/dir1");
        assertEquals(list.get(1).toString(), "src/test/resources/samples/dir2");
        assertEquals(list.get(2).toString(), "src/test/resources/samples/dir3");
        assertEquals(list.get(3).toString(), "src/test/resources/samples/file1");
        assertEquals(list.get(4).toString(), "src/test/resources/samples/file2");
        assertEquals(list.get(5).toString(), "src/test/resources/samples/file3");
        assertEquals(list.get(6).toString(), "src/test/resources/samples/symlink1");
    }
}
