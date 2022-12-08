package com.mangione.continuous.observationproviders.csv;

import java.io.*;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mangione.continuous.observationproviders.ProviderException;

class CsvObservationIterator<T> implements Iterator<T> {
    private final BufferedReader bufferedReader;
    private final String splitVal;
    private final Function<String[], T> factory;
    private String nextLine;

    CsvObservationIterator(File file, String splitVal, Function<String[], T> factory, boolean hasColumnHeader,
            Function<File, BufferedReader> bufferedFunction) {
        this.splitVal = splitVal;
        this.factory = factory;

        try {
            bufferedReader = bufferedFunction.apply(file);
            if (hasColumnHeader)
                bufferedReader.readLine();
        } catch (IOException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            nextLine = bufferedReader.readLine();
        } catch (IOException e) {
            throw new ProviderException(e);
        }
        return nextLine != null;
    }

    @Override
    public T next() {
        if (nextLine == null)
            throw new IllegalStateException("Did you call hasNext? Did you pay attention to the answer?");
        return factory.apply(nextLine.trim().split(splitVal));
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported...");
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        while (hasNext())
            action.accept(next());
    }

    synchronized T getNext() {
        return hasNext() ? next() : null;
    }
        void closeReader() throws IOException {
        bufferedReader.close();
    }
}
