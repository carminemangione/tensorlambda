package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.ProxyValues;

public class RowFilteringObservationProvider<S, T extends ObservationInterface<S>>
		implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<S, T> provider;
	private final Predicate<T> predicate;
	private Integer numberOfLines;

	public RowFilteringObservationProvider(ObservationProviderInterface<S, T> provider,
			Predicate<T> predicate) {
		if (predicate == null)
			throw new IllegalArgumentException("predicate can not be null");
		this.provider = provider;
		this.predicate = predicate;
	}

	@Override
	public long getNumberOfLines() {
		if (numberOfLines == null) {
			int i = 0;
			for (T ignored : this)
				i++;
			numberOfLines = i;
		}
		return numberOfLines;
	}

	@Override
	public ProxyValues getNamedColumns() {
		return null;
	}

	@Nonnull
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private final Iterator<T> iterator = provider.iterator();
			private T next;

			@Override
			public boolean hasNext() {
				fillNextValueIfNeeded();
				return next != null;
			}

			@Override
			public T next() {
				fillNextValueIfNeeded();
				if (next == null)
					throw new IllegalStateException("There is no next there...");
				T nextValue = next;
				next = null;
				return nextValue;
			}

			private void fillNextValueIfNeeded() {
				while (next == null && iterator.hasNext()) {
					T nextTest = iterator.next();
					next = !predicate.test(nextTest) ? nextTest : null;
				}
			}
		};
	}
}
