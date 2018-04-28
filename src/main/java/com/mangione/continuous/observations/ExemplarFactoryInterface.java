package com.mangione.continuous.observations;

public interface ExemplarFactoryInterface<S, I, T extends ExemplarInterface<S, I>>
        extends ObservationFactoryInterface<S, T> {
	T create(S[] data);
}
