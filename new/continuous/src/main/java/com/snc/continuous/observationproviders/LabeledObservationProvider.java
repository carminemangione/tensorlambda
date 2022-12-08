package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.encodings.ProxyValues;

public class LabeledObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {
	private final ObservationProviderInterface<S, T> wrappedProvider;
	private final ProxyValues columnNames;

	@SuppressWarnings("WeakerAccess")
	public LabeledObservationProvider(ObservationProviderInterface<S, T> wrappedProvider, ProxyValues columnNames) {
		this.wrappedProvider = wrappedProvider;
		this.columnNames = columnNames;
	}

	@Override
	public int size() {
		return wrappedProvider.size();
	}

	@Override
	public ProxyValues getNamedColumns() {
		return columnNames;
	}

	@Override
	@Nonnull
	public Iterator<T> iterator() {
		return wrappedProvider.iterator();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		wrappedProvider.forEach(action);
	}

	@Override
	public Spliterator<T> spliterator() {
		return wrappedProvider.spliterator();
	}
}
