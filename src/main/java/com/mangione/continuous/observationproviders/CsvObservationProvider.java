package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactoryInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvObservationProvider<T extends Observation> extends ObservationProvider<T> {

    private final File file;
    private final Map<Integer, VariableCalculator> indexToCalculator;
    private BufferedReader bufferedReader;

    public CsvObservationProvider(File file, ObservationFactoryInterface<T> factory) throws FileNotFoundException {
        this(file, factory, new HashMap<>());
    }

    public CsvObservationProvider(File file, ObservationFactoryInterface<T> factory,
            Map<Integer, VariableCalculator> indexToCalculator) throws FileNotFoundException {
        super(factory);
        this.file = file;
        this.indexToCalculator = indexToCalculator;
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    @Override
    public boolean hasNext() throws IOException {
        boolean ready = false;
        if (bufferedReader != null) {
            ready = bufferedReader.ready();
            if (!ready) {
                reachedEndCloseFileAndClearReader();
            }
        }
        return ready;
    }

    @Override
    public T next() throws Exception {
        String[] nextLine = bufferedReader.readLine().split(",");
        double data[] = translateAllVariables(nextLine);
        return create(data);
    }

    @Override
    public void reset() throws IOException {
        if (bufferedReader != null)
            bufferedReader.close();
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    @Override
    public long getNumberOfLines() throws IOException {
        long numberOfLines = 0;
        while (bufferedReader.ready()) {
            numberOfLines++;
            bufferedReader.readLine();
        }
        reset();
        return numberOfLines;
    }

    private void reachedEndCloseFileAndClearReader() throws IOException {
        bufferedReader.close();
        bufferedReader = null;
    }


    public double[] translateAllVariables(String[] features) {
        List<Double> translatedVariables = new ArrayList<>();

        for (int i = 0; i < features.length; i++) {
            if (indexToCalculator.get(i) != null) {
                double[] variable = indexToCalculator.get(i).calculateVariable(features[i]);
                for (double aVariable : variable) {
                    translatedVariables.add(aVariable);
                }
            } else {
                translatedVariables.add(Double.parseDouble(features[i]));
            }
        }
        double[] translated = new double[translatedVariables.size()];
        for (int i = 0; i < translatedVariables.size(); i++) {
            translated[i] = translatedVariables.get(i);
        }
        return translated;
    }


}
