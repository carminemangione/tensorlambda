package com.mangione.continuous.observationproviders;

import java.io.IOException;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public abstract class ObservationProvider<S, T extends ObservationInterface<S>> {

    private final ObservationFactoryInterface<S, T> factory;

    protected ObservationProvider(ObservationFactoryInterface<S, T> factory) {
        this.factory = factory;
    }

    public abstract boolean hasNext();

    public abstract T next();

    public abstract void reset();

    public abstract long getNumberOfLines();

    protected T create(S data[]) {
        return factory.create(data);
    }
}
