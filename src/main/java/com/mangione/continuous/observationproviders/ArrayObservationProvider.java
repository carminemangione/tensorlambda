package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactoryInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayObservationProvider<S> extends ObservationProvider<S> {
    private final List<S> observations = new ArrayList<>();
    private int current;

    public ArrayObservationProvider(S[][] data, ObservationFactoryInterface<S> observationFactoryInterface) {
        super(observationFactoryInterface);
        for (S[] doubles : data) {
            observations.add(create(doubles));
        }
    }

    public ArrayObservationProvider(ObservationProvider<S> observationProvider,
            ObservationFactoryInterface<S> observationFactoryInterface) throws Exception {
        super(observationFactoryInterface);
        while (observationProvider.hasNext()) {
            observations.add(create(observationProvider.next().getFeatures()));
        }
        observationProvider.reset();
    }

    @Override
    public boolean hasNext()  {
        return current < observations.size();
    }

    @Override
    public T next() {
        return observations.get(current++);
    }

    @Override
    public void reset()  {
        current = 0;
    }

    @Override
    public long getNumberOfLines()  {
        return observations.size();
    }
}
