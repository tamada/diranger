package jp.cafebabe.diranger;

import jp.cafebabe.diranger.impl.Simple;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RangerTest {
    @Test
    public void testDefault() throws IOException {
        Ranger r = new Simple();
        var list = r.list(Entry.of("src/test/resources/samples"),
                new Config.Builder()
                        .respectIgnoreFiles(false)
                        .skipSymlinks(true)
                        .skipHiddenFiles(true).build());
        assertEquals(5, list.size());
        assertEquals(list.get(0).toString(), "src/test/resources/samples/dir3/file3_1");
        assertEquals(list.get(1).toString(), "src/test/resources/samples/dir3/file3_2");
        assertEquals(list.get(2).toString(), "src/test/resources/samples/file1");
        assertEquals(list.get(3).toString(), "src/test/resources/samples/file2");
        assertEquals(list.get(4).toString(), "src/test/resources/samples/file3");
    }

    @Test
    public void testGitIgnore() throws IOException {
        Ranger r = new Simple();
        var list = r.list(Entry.of("src/test/resources/samples"),
                new Config.Builder()
                        .respectIgnoreFiles(true)
                        .skipSymlinks(true)
                        .skipHiddenFiles(true).build());
        assertEquals(2, list.size());
        assertEquals("src/test/resources/samples/file1", list.get(0).toString());
        assertEquals("src/test/resources/samples/file3", list.get(1).toString());
    }

    @Test
    public void testHiddenFiles() throws IOException {
        Ranger r = new Simple();
        var list = r.list(Entry.of("src/test/resources/samples"),
                new Config.Builder()
                        .respectIgnoreFiles(false)
                        .skipSymlinks(true)
                        .skipHiddenFiles(false).build());
        assertEquals(9, list.size());
        assertEquals(list.get(0).toString(), "src/test/resources/samples/.gitignore");
        assertEquals(list.get(1).toString(), "src/test/resources/samples/.hidden_dir1/file0_1");
        assertEquals(list.get(2).toString(), "src/test/resources/samples/.hidden_file1");
        assertEquals(list.get(3).toString(), "src/test/resources/samples/dir3/.gitignore");
        assertEquals(list.get(4).toString(), "src/test/resources/samples/dir3/file3_1");
        assertEquals(list.get(5).toString(), "src/test/resources/samples/dir3/file3_2");
        assertEquals(list.get(6).toString(), "src/test/resources/samples/file1");
        assertEquals(list.get(7).toString(), "src/test/resources/samples/file2");
        assertEquals(list.get(8).toString(), "src/test/resources/samples/file3");
    }


    @Test
    public void testHiddenFileAndSymlinks() throws IOException {
        Ranger r = new Simple();
        var list = r.list(Entry.of("src/test/resources/samples"),
                new Config.Builder()
                        .respectIgnoreFiles(false)
                        .skipSymlinks(false)
                        .skipHiddenFiles(false).build());
        assertEquals(11, list.size());
        assertEquals(list.get(0).toString(), "src/test/resources/samples/.gitignore");
        assertEquals(list.get(1).toString(), "src/test/resources/samples/.hidden_dir1/file0_1");
        assertEquals(list.get(2).toString(), "src/test/resources/samples/.hidden_file1");
        assertEquals(list.get(3).toString(), "src/test/resources/samples/.hidden_symlink1/file0_1");
        assertEquals(list.get(4).toString(), "src/test/resources/samples/dir3/.gitignore");
        assertEquals(list.get(5).toString(), "src/test/resources/samples/dir3/file3_1");
        assertEquals(list.get(6).toString(), "src/test/resources/samples/dir3/file3_2");
        assertEquals(list.get(7).toString(), "src/test/resources/samples/file1");
        assertEquals(list.get(8).toString(), "src/test/resources/samples/file2");
        assertEquals(list.get(9).toString(), "src/test/resources/samples/file3");
        assertEquals(list.get(10).toString(), "src/test/resources/samples/symlink1");
    }
}
