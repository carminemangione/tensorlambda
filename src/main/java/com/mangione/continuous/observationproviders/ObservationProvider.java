package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

import java.util.List;

public abstract class ObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S,T> {

    private final ObservationFactoryInterface<S, ? extends T> factory;

    protected ObservationProvider(ObservationFactoryInterface<S,  ? extends T> factory) {
        this.factory = factory;
    }

	public T create(List<S> data) {

		return factory.create(data, null);
    }

}
