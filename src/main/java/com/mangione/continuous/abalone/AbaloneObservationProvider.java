package com.mangione.continuous.abalone;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.mangione.continuous.observationproviders.CsvObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observationproviders.VariableCalculator;
import com.mangione.continuous.observations.Exemplar;
import com.mangione.continuous.observations.ObservationFactoryInterface;

public class AbaloneObservationProvider<T extends Exemplar> extends ObservationProvider<T> {

    private final CsvObservationProvider<T> observationProvider;

    public AbaloneObservationProvider(String abaloneFile, ObservationFactoryInterface<T> factory) throws Exception {
        super(factory);
        final URL url = AbaloneObservationProvider.class.getClassLoader()
                .getResource(abaloneFile);
        if (url == null)
            throw new IllegalArgumentException("Could not find the file:" + abaloneFile);
        File file = new File(url.toURI());

        Map<Integer, VariableCalculator> calculators = new HashMap<>();
        calculators.put(0, new SexVariableCalculator());

        observationProvider = new CsvObservationProvider<>(file, factory, calculators);
    }

    @Override
    public boolean hasNext() throws Exception {
        return observationProvider.hasNext();
    }

    @Override
    public T next() throws Exception {
        return observationProvider.next();
    }

    @Override
    public void reset() throws Exception {
        observationProvider.reset();
    }

    @Override
    public long getNumberOfLines() throws IOException {
        return observationProvider.getNumberOfLines();
    }
}
