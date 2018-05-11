package com.mangione.continuous.observationproviders;

import java.util.Iterator;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationInterface;

public interface ObservationProviderInterface <S, T extends ObservationInterface<S>> extends Iterable<T> {

	long getNumberOfLines();

	T create(S data[]);

	@Override
	@Nonnull
	Iterator<T> iterator();
}
