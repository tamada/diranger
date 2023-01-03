package jp.cafebabe.diranger.impl;

import jp.cafebabe.diranger.Config;
import jp.cafebabe.diranger.Entry;
import jp.cafebabe.diranger.ignorefiles.GitIgnoreVisitor;

import java.nio.file.FileVisitor;

public class VisitorFactory {
    private Config config;

    public VisitorFactory(Config config) {
        this.config = config;
    }

    public FileVisitor<Entry> create(FileVisitor<Entry> parent) {
        if(config.respectIgnoreFiles())
            return new GitIgnoreVisitor(parent);
        return parent;
    }

}
