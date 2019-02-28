package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class ColumnFilteringObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private final ObservationProviderInterface<S, T> provider;
	private final Set<Integer> columnsToFilter;
	private final ObservationFactoryInterface<S, T> factory;

	public ColumnFilteringObservationProvider(ObservationProviderInterface<S, T> provider, int[] columnsToFilter,
											  ObservationFactoryInterface<S, T> factory) {
		this.provider = provider;
		this.columnsToFilter = new HashSet<>();
		this.columnsToFilter.addAll(Arrays.stream(columnsToFilter)
				.boxed()
				.collect(Collectors.toList()));
		this.factory = factory;
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
				int[] filteredColumns = next.getColumnIndexes().stream()
						.filter(column -> !columnsToFilter.contains(column))
						.mapToInt(Integer::intValue)
						.toArray();

				List<S> filteredFeatures = Arrays.stream(filteredColumns)
						.boxed()
						.map(next::getFeature)
						.collect(Collectors.toList());
				return factory.create(filteredFeatures, filteredColumns);
			}
		};
	}
}
