package jp.cafebabe.traverser;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class FileSystemTraverserTest {
    private TraverserBuilder builder = new TraverserBuilder();

    @Test
    public void testStreamOfDirectory() throws Exception {
        Traverser traverser = builder.build(Paths.get("src/test/resources"));
        String[] results = traverser.stream()
                .map(path -> removeBasePath(traverser.basePath(), path)).toArray(size -> new String[size]);

        assertThat(results, is(arrayContainingInAnyOrder("archive.jar", "a.txt", "go/hello.go", "java/hello.class", "java/hello.java", "node/hello.js")));
    }

    @Test
    public void testStreamOfJar() throws Exception {
        Traverser traverser = builder.build(Paths.get("src/test/resources/archive.jar"));
        String[] results = traverser.stream()
                .map(path -> removeBasePath(traverser.basePath(), path)).toArray(size -> new String[size]);

        assertThat(results, is(arrayContainingInAnyOrder("/META-INF/MANIFEST.MF", "/a.txt", "/go/hello.go", "/java/hello.class", "/java/hello.java", "/node/hello.js")));
    }

    @Test
    public void testRemoveBasePath() {
        Path basePath = Paths.get("/base/path");
        
        assertThat(removeBasePath(basePath, Paths.get("/base/path/a.txt")), is("a.txt"));
        assertThat(removeBasePath(basePath, Paths.get("b.txt")), is("b.txt"));
        assertThat(removeBasePath(basePath, Paths.get("/base/path/of/c.txt")), is("of/c.txt"));
    }

    private String removeBasePath(Path basePath, Path path) {
        String basePathString = basePath.toString();
        String pathString = path.toString();
        if(pathString.startsWith(basePathString)) {
            return pathString.substring(basePathString.length() + 1);
        }
        return pathString;
    }
}
