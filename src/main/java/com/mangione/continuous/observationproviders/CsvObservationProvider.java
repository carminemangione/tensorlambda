package com.mangione.continuous.observationproviders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class CsvObservationProvider<S, T extends ObservationInterface<S>> extends ObservationProvider<S, T> {

    private final File file;
    private final Map<Integer, VariableCalculator<S>> indexToCalculator;
    private final VariableCalculator<S> defaultCalculator;
    private final ArraySupplier<S> arraySupplier;

    private BufferedReader bufferedReader;

    public CsvObservationProvider(File file, ObservationFactoryInterface<S, T> factory,
            VariableCalculator<S> defaultCalculator, ArraySupplier<S> arraySupplier) throws FileNotFoundException {
        this(file, factory, new HashMap<>(), defaultCalculator, arraySupplier);
    }

    private CsvObservationProvider(File file, ObservationFactoryInterface<S, T> factory,
            Map<Integer, VariableCalculator<S>> indexToCalculator,
            VariableCalculator<S> defaultCalculator, ArraySupplier<S> arraySupplier) throws FileNotFoundException {
        super(factory);
        this.file = file;
        this.indexToCalculator = indexToCalculator;
        this.bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        this.defaultCalculator = defaultCalculator;
        this.arraySupplier = arraySupplier;
    }

    @Override
    public boolean hasNext() {
        boolean ready = false;
        try {
            if (bufferedReader != null) {
                ready = bufferedReader.ready();
                if (!ready) {
                    reachedEndCloseFileAndClearReader();
                }
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
            nextLine = bufferedReader.readLine().split(",");
        } catch (IOException e) {
            throw new ProviderException(e);
        }
        S data[] = translateAllVariables(nextLine);
        return create(data);
    }

    @Override
    public void reset()  {
        try {
            if (bufferedReader != null)
                bufferedReader.close();
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (IOException e) {
            throw new ProviderException(e);
        }
    }

    @Override
    public long getNumberOfLines()  {
        long numberOfLines = 0;
        try {
            while (bufferedReader.ready()) {
                numberOfLines++;
                bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new ProviderException(e);
        }
        reset();
        return numberOfLines;
    }

    private void reachedEndCloseFileAndClearReader() throws IOException {
        bufferedReader.close();
        bufferedReader = null;
    }


    private S[] translateAllVariables(String[] features) {
        List<S> translatedVariables = new ArrayList<>();
        for (int i = 0; i < features.length; i++) {
            translatedVariables.addAll(calculateVariableWithIndexedCalcuatorOrDefault(features[i], i));
        }
        return translatedVariables.toArray(arraySupplier.get(translatedVariables.size()));
    }

    private List<S> calculateVariableWithIndexedCalcuatorOrDefault(String variable, int index) {
        return indexToCalculator.get(index) != null ?
            indexToCalculator.get(index).calculateVariable(variable) :
                defaultCalculator.calculateVariable(variable);
    }

}

