package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactoryInterface;

import java.io.IOException;

public abstract class ObservationProvider<S> {

    private final ObservationFactoryInterface<S> factory;

    protected ObservationProvider(ObservationFactoryInterface<S> factory) {
        this.factory = factory;
    }

    public abstract boolean hasNext() throws Exception;

    public abstract S next() throws Exception;

    public abstract void reset() throws Exception;

    public abstract long getNumberOfLines() throws IOException;

    protected Observation<S> create(S data[]) {
        return factory.create(data);
    }
}
