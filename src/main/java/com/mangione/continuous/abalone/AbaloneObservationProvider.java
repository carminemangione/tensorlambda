package com.mangione.continuous.abalone;

import com.mangione.continuous.observationproviders.*;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.observations.ExemplarInterface;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AbaloneObservationProvider extends ObservationProvider<Double, ExemplarInterface<Double, Integer>> {

    private final CsvObservationProvider<Double, ExemplarInterface<Double, Integer>> observationProvider;

    public AbaloneObservationProvider(String abaloneFile) throws Exception {
        super(new DiscreteExemplarFactory());
        final URL url = AbaloneObservationProvider.class.getClassLoader()
                .getResource(abaloneFile);
        if (url == null)
            throw new IllegalArgumentException("Could not find the file:" + abaloneFile);
        File file = new File(url.toURI());

        Map<Integer, VariableCalculator> calculators = new HashMap<>();
        calculators.put(0, new SexVariableCalculator());

        observationProvider = new CsvObservationProvider<>(file,
                new DiscreteExemplarFactory(),
                new DoubleVariableCalculator(),
                new DoubleArraySupplier());
    }

    @Override
    public boolean hasNext() {
        return observationProvider.hasNext();
    }

    @Override
    public ExemplarInterface<Double, Integer> next() {
        return observationProvider.next();
    }

    @Override
    public void reset() {
        observationProvider.reset();
    }

    @Override
    public long getNumberOfLines() {
        return observationProvider.getNumberOfLines();
    }
}
