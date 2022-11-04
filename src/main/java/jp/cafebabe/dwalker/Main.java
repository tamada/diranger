package jp.cafebabe.dwalker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class Main {
    private void run(String[] args) throws IOException {
        Arrays.stream(args)
                .forEach(this::listUp);
    }

    private void listUp(String base) {
        var walker = new WalkerBuilder(Path.of(base)).build();
        System.out.printf("%s%n", base);
        walker.stream().forEach(p -> System.out.printf("\t%s%n", p));
    }

    public static void main(String[] args) throws IOException {
        new Main().run(args);
    }
}
