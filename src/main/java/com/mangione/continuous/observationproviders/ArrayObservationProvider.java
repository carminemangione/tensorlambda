package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactoryInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayObservationProvider<T extends Observation> extends ObservationProvider<T> {
    private final List<T> observations = new ArrayList<>();
    private int current;

    public ArrayObservationProvider(double[][] data, ObservationFactoryInterface<T> observationFactoryInterface) {
        super(observationFactoryInterface);
        for (double[] doubles : data) {
            observations.add(create(doubles));
        }
    }

    public ArrayObservationProvider(ObservationProvider observationProvider,
            ObservationFactoryInterface<T> observationFactoryInterface) throws Exception {
        super(observationFactoryInterface);
        while (observationProvider.hasNext()) {
            observations.add(create(observationProvider.next().getFeatures()));
        }
        observationProvider.reset();
    }

    @Override
    public boolean hasNext() throws IOException {
        return current < observations.size();
    }

    @Override
    public T next() throws IOException {
        return observations.get(current++);
    }

    @Override
    public void reset() throws IOException {
        current = 0;
    }

    @Override
    public long getNumberOfLines() throws IOException {
        return observations.size();
    }
}
