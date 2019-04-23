package com.mangione.continuous.observationproviders.csv;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observationproviders.ProviderException;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.dense.Observation;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;

public class CsvObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

    private final File file;
    private final Function<String[], T> csvToObservationFactory;
    private boolean hasColumnHeader;
    private final ProxyValues namedColumns = new ProxyValues();
    private final String charVal;
    private final Function<File, BufferedReader> bufferedFunction;

    private final int estimatedSizeForSpliterator;
    private final int minimumBatchSizeForSpliterator;

    public static ObservationProviderInterface<String, ObservationInterface<String>> stringCsvObservationProvider(File file,
            boolean hasHeader) {
        return new CsvObservationProvider<>(file, strings-> new Observation<>(Arrays.asList(strings)), hasHeader);
    }

    public CsvObservationProvider(File file, Function<String[], T> csvToObservationFactory, boolean hasColumnHeader) {
        this(file, csvToObservationFactory, hasColumnHeader, ",",
                CsvObservationProvider::getBufferedReader, 1000, 100);
    }

    public CsvObservationProvider(File file, Function<String[], T> csvToObservationFactory, boolean hasColumnHeader,
            String splitValue, Function<File, BufferedReader> bufferedFunction, int estimatedSizeForSpliterator, int minimumBatchSizeForSpliterator) {
        this.file = file;
        this.csvToObservationFactory = csvToObservationFactory;
        this.hasColumnHeader = hasColumnHeader;
        this.charVal = splitValue;
        this.bufferedFunction = bufferedFunction;
        this.estimatedSizeForSpliterator = estimatedSizeForSpliterator;
        this.minimumBatchSizeForSpliterator = minimumBatchSizeForSpliterator;

        if (hasColumnHeader)
            fillNamedColumns();
    }

    private void fillNamedColumns() {
        try {
            BufferedReader readerForCSVFile = getBufferedReader(file);
            String[] names = readerForCSVFile.readLine().split(charVal);
            IntStream.range(0, names.length).forEach(i -> namedColumns.addPair(i, names[i]));
        } catch (IOException e) {
            throw new ProviderException(e);
        }
    }

    @NotNull
    private static BufferedReader getBufferedReader(File file) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return new CsvObservationIterator<>(file, charVal, csvToObservationFactory, hasColumnHeader, bufferedFunction);
    }

    @Override
    public Spliterator<T> spliterator() {
        Spliterator<T> spliterator = new CsvObservationSpliterator<>(file, charVal, csvToObservationFactory, hasColumnHeader,
                estimatedSizeForSpliterator, minimumBatchSizeForSpliterator, bufferedFunction);
        return spliterator;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T stringObservationInterface : this) {
            action.accept(stringObservationInterface);
        }
    }

    public ProxyValues getNamedColumns() {
        return namedColumns;
    }

}

