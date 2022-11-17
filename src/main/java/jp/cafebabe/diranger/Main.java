package jp.cafebabe.diranger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 *
 */
public class Main {
    private void run(String[] args) throws IOException {
        Config config = new Config.Builder().build();
        Arrays.stream(args)
                .map(dir -> Path.of(dir))
                .forEach(p -> listUp(p, config));
    }

    private void listUp(Path base, Config config) {
        try {
            // listUpImplIterator(base, config);
            listUpImplStream(base, config);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void listUpImplStream(Path base, Config config) throws IOException {
        var ranger = RangerBuilder.build(RangerBuilder.Type.Queue);
        ranger.stream(base, config)
                .forEach(e -> System.out.printf("path: %s%n", e.path()));
    }

    private void listUpImplIterator(Path base, Config config) throws IOException {
        var ranger = RangerBuilder.build(RangerBuilder.Type.Queue);
        for(Entry entry: ranger.iterable(base, new Config.Builder().build())) {
            System.out.printf("entry: %s%n", entry.path());
        }
    }

    public static void main(String[] args) throws IOException {
        new Main().run(args);
    }
}
