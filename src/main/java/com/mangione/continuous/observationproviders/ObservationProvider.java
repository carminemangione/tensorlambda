package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactoryInterface;

import java.io.IOException;

public abstract class ObservationProvider<T extends Observation> {

    private final ObservationFactoryInterface<T> factory;

    protected ObservationProvider(ObservationFactoryInterface<T> factory) {
        this.factory = factory;
    }

    public abstract boolean hasNext() throws Exception;

    public abstract T next() throws Exception;

    public abstract void reset() throws Exception;

    public abstract long getNumberOfLines() throws IOException;

    protected T create(double data[]) {
        return factory.create(data);
    }
}
