package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;
import java.util.Iterator;

public interface ObservationProviderInterface <S, T extends ObservationInterface<S>> extends Iterable<T> {

	long getNumberOfLines();

	@Override
	@Nonnull
	Iterator<T> iterator();
}
