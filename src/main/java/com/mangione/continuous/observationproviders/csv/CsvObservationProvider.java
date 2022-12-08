package com.mangione.continuous.observationproviders.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observationproviders.ProviderException;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.encodings.ProxyValues;

public class CsvObservationProvider<FEATURE, OBSERVATION extends ObservationInterface<FEATURE>> implements ObservationProviderInterface<FEATURE, OBSERVATION> {

    private final File file;
    private final Function<String[], OBSERVATION> csvToObservationFactory;
    private final boolean hasColumnHeader;
    private final ProxyValues namedColumns = new ProxyValues();
    private final String charVal;
    private final Function<File, BufferedReader> bufferedFunction;

    private final int estimatedSizeForSpliterator;
    private final int minimumBatchSizeForSpliterator;

    public CsvObservationProvider(File file, Function<String[], OBSERVATION> csvToObservationFactory, boolean hasColumnHeader) {
        this(file, csvToObservationFactory, hasColumnHeader, ",",
                defaultBufferedReader(), 1000, 100);
    }

    public CsvObservationProvider(File file, Function<String[], OBSERVATION> csvToObservationFactory, boolean hasColumnHeader,
            Function<File, BufferedReader> fileReader) {
        this(file, csvToObservationFactory, hasColumnHeader, ",",
                fileReader, 1000, 100);
    }

    public CsvObservationProvider(File file, Function<String[], OBSERVATION> csvToObservationFactory, boolean hasColumnHeader,
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
            BufferedReader readerForCSVFile = bufferedFunction.apply(file);
            String[] names = readerForCSVFile.readLine().split(charVal);
            IntStream.range(0, names.length).forEach(i -> namedColumns.addPair(i, names[i]));
        } catch (IOException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    @Nonnull
    public Iterator<OBSERVATION> iterator() {
        return new CsvObservationIterator<>(file, charVal, csvToObservationFactory, hasColumnHeader, bufferedFunction);
    }

    @Override
    public Spliterator<OBSERVATION> spliterator() {
        return new CsvObservationInterleavedSpliterator<>(file, csvToObservationFactory, hasColumnHeader,
                estimatedSizeForSpliterator, minimumBatchSizeForSpliterator, bufferedFunction, charVal);
    }

    @Override
    public void forEach(Consumer<? super OBSERVATION> action) {
        for (OBSERVATION stringObservationInterface : this) {
            action.accept(stringObservationInterface);
        }
    }

    public ProxyValues getNamedColumns() {
        return namedColumns;
    }

    private static Function<File, BufferedReader> defaultBufferedReader() {
        return f -> {
            try {
                return new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
    }

}

