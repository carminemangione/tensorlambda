package com.mangione.continuous.observationproviders.csv;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mangione.continuous.observationproviders.ProviderException;

class CsvObservationIterator<T> implements Iterator<T> {
    private final BufferedReader bufferedReader;
    private final String splitVal;
    private final Function<String[], T> factory;
    private boolean closed;

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
        boolean ready;
        try {
            ready = !closed && bufferedReader.ready();
            if(!ready && !closed) {
                bufferedReader.close();
                closed = true;
            }
        } catch (IOException e) {
            throw new ProviderException(e);
        }
        return ready;
    }

    @Override
    public T next() {
        String[] nextLine;
        try {
            nextLine = bufferedReader.readLine().split(splitVal);
        } catch (IOException e) {
            throw new ProviderException(e);
        }
        return factory.apply(nextLine);
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
