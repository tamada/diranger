package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitIgnoreFileTest {
    @Test
    public void testBasic() throws IOException {
        IgnoreFile ignore = new GitIgnoreFile.Builder()
                .build(Entry.of("src/test/resources/samples/.gitignore"));
        assertTrue(ignore.isIgnore(Entry.of("src/test/resources/samples/dir2")));
        assertTrue(ignore.isIgnore(Entry.of("src/test/resources/samples/file2")));
        assertTrue(ignore.isIgnore(Entry.of("src/test/resources/samples/dir1/subdir1_2")));
        assertTrue(ignore.isIgnore(Entry.of("src/test/resources/samples/dir3/file3_2")));

        assertNullOrFalse(ignore.isIgnore(Entry.of("src/test/resources/samples/dir1")));
        assertNullOrFalse(ignore.isIgnore(Entry.of("src/test/resources/samples/dir3")));
        assertNullOrFalse(ignore.isIgnore(Entry.of("src/test/resources/samples/file1")));
    }

    private void assertNullOrFalse(Boolean flag) {
        assertFalse(flag != null && flag);
    }
}
