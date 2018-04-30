package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationInterface;

public interface ObservationProviderInterface <S, T extends ObservationInterface<S>> {

	boolean hasNext();

	T next();

	void reset();

	long getNumberOfLines();

	T create(S data[]);
}
