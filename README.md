# Traverser

After released Java SE 8, stream API becomes general in the programming.
However, Java SE does not have `stream()` method for traversing specified directories.
This library supports stream API for directory traversing.

## Usage

### Simple Use Case

```java
Path path = Paths.get("some/path/of/directory/or/jar/file");
Traverser traverser = new TraverserBuilder().build(path);
try(Stream<Path> stream = traverser.stream()){
    stream.forEach(System.out::println);
}
```

### stream with BasicFileAttributes

In [Simple use case](#simple-use-case), we can only obtain `Path` object.
We may need file attributes of corresponding path.
In such case, we can use `buildForFileItem` method of `TraverserBuilder` class.


```java
Path path = Paths.get("some/path/of/directory/or/jar/file");
Traverser2 traverser = new TraverserBuilder().buildForFileItem(path);
try(Stream<FileItem> stream = traverser.stream()){
    stream.map(item -> item.path()).forEach(System.out::println);
}
```

`FileItem` is just simple wrapper class like below.


```java
public class FileItem{
    Path path();
    Optional<BasicFileAttributes> attributes();
}
```

## Install

* repository
    * `https://maven.pkg.github.com/tamada`
* group id
    * `jp.cafebabe`
* artifact id
    * traverser
* version
    * `1.0.0`


### pom.xml

```xml
...
  <repositories>
    <repository>
      <id>github.tamada</id>
      <url>https://maven.pkg.github.com/tamada</url>
    </repository>
  </repositories>
...
  <dependencies>
    <dependency>
      <groupId>jp.cafebabe</groupId>
      <artifactId>traverser</artifactId>
      <version>1.0.0</version>
      <scope>compile</scope>
    </dependency>
...
  </dependencies>
...
```

