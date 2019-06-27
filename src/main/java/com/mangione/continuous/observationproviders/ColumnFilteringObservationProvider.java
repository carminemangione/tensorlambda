package com.mangione.continuous.observationproviders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.ObservationInterface;

public class ColumnFilteringObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<S, T> provider;
	private final Set<Integer> columnsToFilter;
	private final BiFunction<List<S>, List<Integer>, T> factory;

	@SuppressWarnings("WeakerAccess")
	public ColumnFilteringObservationProvider(ObservationProviderInterface<S, T> provider, int[] columnsToFilter,
											  BiFunction<List<S>, List<Integer>, T> valuesColumnsToObservationFactory) {
		this.provider = provider;
		this.columnsToFilter = new HashSet<>();
		this.columnsToFilter.addAll(Arrays.stream(columnsToFilter)
				.boxed()
				.collect(Collectors.toList()));
		this.factory = valuesColumnsToObservationFactory;

	}

	@Nonnull
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private final Iterator<T> iterator = provider.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public T next() {
				T next = iterator.next();
				List<Integer> columnIndexes = next.getColumnIndexes().stream()
						.filter(column -> !columnsToFilter.contains(column))
						.collect(Collectors.toList());

				List<S> filteredFeatures = columnIndexes.stream()
						.map(next::getFeature)
						.collect(Collectors.toList());

				return factory.apply(filteredFeatures,columnIndexes);
			}
		};
	}

}
