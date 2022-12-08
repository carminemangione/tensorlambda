package com.mangione.continuous.observationproviders;

import java.util.Iterator;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.encodings.ProxyValues;

public class RowFilteringObservationProvider<FEATURE, OBSERVATION extends ObservationInterface<FEATURE>> implements ObservationProviderInterface<FEATURE, OBSERVATION> {

	private final ObservationProviderInterface<FEATURE, OBSERVATION> provider;
	private final Predicate<OBSERVATION> filteringPredicate;
	private Integer numberOfLines;

	public RowFilteringObservationProvider(ObservationProviderInterface<FEATURE, OBSERVATION> provider, Predicate<OBSERVATION> filteringPredicate) {
		if (filteringPredicate == null)
			throw new IllegalArgumentException("filteringPredicate can not be null");
		this.provider = provider;
		this.filteringPredicate = filteringPredicate;
	}

	@Override
	public int size() {
		if (numberOfLines == null) {
			int i = 0;
			for (OBSERVATION ignored : this)
				i++;
			numberOfLines = i;
		}
		return numberOfLines;
	}

	@Override
	public ProxyValues getNamedColumns() {
		return null;
	}

	@Override
	@Nonnull
	public Iterator<OBSERVATION> iterator() {
		return new Iterator<>() {
			private final Iterator<OBSERVATION> iterator = provider.iterator();
			private OBSERVATION next;

			@Override
			public boolean hasNext() {
				fillNextValueIfNeeded();
				return next != null;
			}

			@Override
			public OBSERVATION next() {
				if (next == null)
					throw new IllegalStateException("There is no next there... Did you call hasNext?");
				return next;
			}

			private void fillNextValueIfNeeded() {
				next = null;
				while (next == null && iterator.hasNext()) {
					OBSERVATION nextTest = iterator.next();
					next = !filteringPredicate.test(nextTest) ? nextTest : null;
				}
			}
		};
	}
}
