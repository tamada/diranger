package jp.cafebabe.traverser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    private final Traverser traverser;

    public Main(String[] args) throws IOException{
        Path path = Paths.get(args[0]);
        traverser = new TraverserBuilder().build(path); 
    }

    public void run() throws IOException{
        try(Stream<Path> stream = traverser.stream()){
            stream.forEach(System.out::println);
        }
    }

    public static void main(String[] args) throws IOException{
        new Main(args).run();
    }
}
