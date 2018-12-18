package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.ProxyValues;

public class ColumnFilteringObservationProvider<S, T extends ObservationInterface<S>> implements
	ObservationProviderInterface<S, T> {



	@Override
	public long getNumberOfLines() {
		return 0;
	}

	@Override
	public ProxyValues getNamedColumns() {
		return null;
	}

	@Nonnull
	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public void forEach(Consumer<? super T> action) {

	}
}
