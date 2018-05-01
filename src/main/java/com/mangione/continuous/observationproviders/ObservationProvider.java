package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

public abstract class ObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

    private final ObservationFactoryInterface<S, T> factory;

    protected ObservationProvider(ObservationFactoryInterface<S, T> factory) {
        this.factory = factory;
    }

	@Override
	public T create(S data[]) {
        return factory.create(data);
    }
}
