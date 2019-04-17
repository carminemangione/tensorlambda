package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.dense.Observation;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class CsvObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

    private final File file;
    private final Function<String[], T> csvToObservationFactory;
    private boolean hasColumnHeader;
    private final ProxyValues namedColumns = new ProxyValues();
    private String charVal = ",";


    public static ObservationProviderInterface<String, ObservationInterface<String>> stringCsvObservationProvider(File file, boolean b) {
        return new CsvObservationProvider<>(file, strings-> new Observation<>(Arrays.asList(strings)), b);
    }

    @SuppressWarnings("WeakerAccess")
    public CsvObservationProvider(File file, Function<String[], T> csvToObservationFactory, boolean hasColumnHeader) {
        this.file = file;
        this.csvToObservationFactory = csvToObservationFactory;
        this.hasColumnHeader = hasColumnHeader;

        if (hasColumnHeader)
            fillNamedColumns();
    }

    private void fillNamedColumns() {
        try {
            BufferedReader readerForCSVFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String[] names = readerForCSVFile.readLine().split(charVal);
            IntStream.range(0, names.length).forEach(i -> namedColumns.addPair(i, names[i]));
        } catch (IOException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return new CsvObservationIterator<>(file, charVal, csvToObservationFactory, hasColumnHeader);
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

