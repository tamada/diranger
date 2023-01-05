package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import jp.cafebabe.diranger.Entry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParallelModelTest {
    @Test
    public void testEmptyDirectory() throws Exception {
        var ranger = new ParallelModel();
        var list = ranger.list(Entry.of("src/test/resources/emptydir"),
                new Config.Builder().build());
        assertEquals(0, list.size());
    }

    @Test
    public void testBasic() throws Exception {
        var ranger = new ParallelModel();
        var list = ranger.list(Entry.of("src/test/resources/samples"),
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
}
