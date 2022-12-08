package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
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
	}

	@Override
	@Nonnull
	Iterator<OBSERVATION> iterator();

	@Override
	default Spliterator<OBSERVATION> spliterator() {
		return Spliterators.spliteratorUnknownSize(iterator(), 0);
	}
}
