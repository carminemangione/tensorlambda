package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.ObservationInterface;

public interface ObservationProviderInterface <S, T extends ObservationInterface<S>> extends Iterable<T> {
	default long getNumberOfLines() {
		AtomicInteger count = new AtomicInteger();
		forEach(t -> count.getAndIncrement());
		return count.get();
	}

	default ProxyValues getNamedColumns() {
		return new ProxyValues();
	}

	default int getNumberOfColumns() {
		Iterator<T> iterator = iterator();
		if (!iterator.hasNext())
			throw new IllegalStateException("Can not get number of columns as provider is empty.");
		return iterator.next().getFeatures().size();
	}

	@Override
	@Nonnull
	Iterator<T> iterator();

	@Override
	default Spliterator<T> spliterator() {
		return Spliterators.spliteratorUnknownSize(iterator(), 0);
	}
}
