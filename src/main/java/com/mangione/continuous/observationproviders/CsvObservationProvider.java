package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class CsvObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

    private final File file;
    private final Function<String[], T> csvToObservationFactory;
    private boolean hasColumnHeader;
    private final ProxyValues namedColumns = new ProxyValues();
    private String charVal = ",";

    public static CsvObservationProvider<String, ? extends ObservationInterface<String>> stringCsvObservationProvider(File file, boolean hasColumnHeader) {
        return new CsvObservationProvider<>(file, strings -> new Observation<>(Arrays.asList(strings)), hasColumnHeader);
    }

    public CsvObservationProvider(File file, Function<String[], T> csvToObservationFactory, boolean hasColumnHeader) {
        this.file = file;
        this.csvToObservationFactory = csvToObservationFactory;
        this.hasColumnHeader = hasColumnHeader;

        if (hasColumnHeader)
            fillNamedColumns();
    }

    private void fillNamedColumns() {
        try {
            BufferedReader readerForCSVFile = createReaderForCSVFile();
            String[] names = readerForCSVFile.readLine().split(charVal);
            IntStream.range(0, names.length).forEach(i -> namedColumns.addPair(i, names[i]));
        } catch (IOException e) {
            throw new ProviderException(e);
        }

    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        CsvObservationIterator csvObservationIterator = new CsvObservationIterator();
        csvObservationIterator.setChar(charVal);
        return csvObservationIterator;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T stringObservationInterface : this) {
            action.accept(stringObservationInterface);
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException("Spliterator is not supported...");
    }

    public ProxyValues getNamedColumns() {
        return namedColumns;
    }

    private BufferedReader createReaderForCSVFile() throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));

    }

    private class CsvObservationIterator implements Iterator<T> {
        private final BufferedReader bufferedReader;
        private String splitVal = ",";

        private CsvObservationIterator() {
            try {
                bufferedReader = createReaderForCSVFile();
                if (hasColumnHeader)
                    bufferedReader.readLine();
            } catch (IOException e) {
                throw new ProviderException(e);
            }
        }

        void setChar(String val) {
            splitVal = val;
        }

        @Override
        public boolean hasNext() {
            boolean ready = false;
            try {
                if (bufferedReader != null) {
                    ready = bufferedReader.ready();
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
            return csvToObservationFactory.apply(nextLine);
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
    }
}

