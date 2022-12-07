package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
<<<<<<< HEAD
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.ObservationInterface;

public interface ObservationProviderInterface <S, T extends ObservationInterface<S>> extends Iterable<T> {
	default long getNumberOfLines() {
		AtomicInteger count = new AtomicInteger();
		forEach(t -> count.getAndIncrement());
		return count.get();
=======
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;

import com.mangione.continuous.encodings.ProxyValues;
import com.mangione.continuous.observations.ObservationInterface;

public interface ObservationProviderInterface <FEATURE, OBSERVATION extends ObservationInterface<FEATURE>> extends Iterable<OBSERVATION> {
	default int size() {
		return (int)StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false)
				.count();
	}

	default Stream<OBSERVATION> getStream() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false);
	}

	default ProxyValues getNamedColumns() {
		return new ProxyValues();
	}

	default int getNumberOfFeatures() {
		Iterator<OBSERVATION> iterator = iterator();
		if (!iterator.hasNext())
			throw new IllegalStateException("Can not get number of columns as provider is empty.");
		return iterator.next().numberOfFeatures();
>>>>>>> 73d9563 (Migrated file changes from the source.)
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
<<<<<<< HEAD
	Iterator<T> iterator();

	@Override
	default Spliterator<T> spliterator() {
=======
	Iterator<OBSERVATION> iterator();

	@Override
	default Spliterator<OBSERVATION> spliterator() {
>>>>>>> 73d9563 (Migrated file changes from the source.)
		return Spliterators.spliteratorUnknownSize(iterator(), 0);
	}
}
