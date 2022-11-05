package jp.cafebabe.diranger;

import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;

class EntryIterator implements Iterator<Entry> {
    private final Iterator<Path> iterator;
    private final FileSystemProvider provider;

    public EntryIterator(Iterator<Path> iterator, FileSystemProvider provider) {
        this.iterator = iterator;
        this.provider = provider;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Entry next() {
        Path next = iterator.next();
        return new Entry(next, provider);
    }
}
