package jp.cafebabe.diranger.ignorefiles;

import jp.cafebabe.diranger.Entry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IgnoreFiles implements IgnoreFile {
    private final List<IgnoreFile> files;

    IgnoreFiles(Stream<IgnoreFile> files) {
        this.files = files.collect(Collectors.toList());
    }

    @Override
    public boolean isIgnore(Entry path) {
        return files.stream()
                .anyMatch(ignore -> ignore.isIgnore(path));
    }
}
