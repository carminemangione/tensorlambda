package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.NamedColumns;
import com.mangione.continuous.observations.ObservationInterface;

public interface ObservationProviderInterface <S, T extends ObservationInterface<S>> extends Iterable<T> {
	default long getNumberOfLines() {
		AtomicInteger count = new AtomicInteger();
		forEach(t -> count.getAndIncrement());
		return count.get();
	}

	default NamedColumns getNamedColumns() {
		return new NamedColumns();
	}

	@Override
	@Nonnull
	Iterator<T> iterator();
}
