package com.mangione.continuous.observationproviders;

import java.util.ArrayList;
import java.util.List;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class ArrayObservationProvider<S, T extends ObservationInterface<S>> extends ObservationProvider<S, T> {
    private final List<T> observations = new ArrayList<>();
    private int current;

    public ArrayObservationProvider(S[][] data, ObservationFactoryInterface<S, T> observationFactoryInterface) {
        super(observationFactoryInterface);
        for (S[] doubles : data) {
            observations.add(create(doubles));
        }
    }

    public ArrayObservationProvider(ObservationProvider<S, T> observationProvider,
            ObservationFactoryInterface<S, T> observationFactoryInterface) {
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
