# Traverser

Java 8 以降，`Stream`を使っての処理を書くことが多くなった．
その時に，指定したディレクトリ以下のファイルを `Stream`で取得できなかったので，書いてみた．

## Usage

```java
Path path = Paths.get("some/path/of/directory");
Traverser traverser = new TraverserBuilder().build(path);
try(Stream<FileItem> stream = traverser.stream()){
    stream.forEach(System.out::println);
}
```

ね，簡単でしょう？

ちなみに，`FileItem`は 2つのメソッドを持ちます．


```java
public class FileItem{
    Path path();
    BasicFileAttributes attributes();
}
```
