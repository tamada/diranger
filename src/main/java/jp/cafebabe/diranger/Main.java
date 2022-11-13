package jp.cafebabe.diranger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class Main {
    private void run(String[] args) throws IOException {
        Arrays.stream(args)
                .forEach(this::listUp);
    }

    private void listUp(String base) {

    }

    public static void main(String[] args) throws IOException {
        new Main().run(args);
    }
}
