package com.mangione.continuous.observationproviders;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.util.LoggingTimer;

public class ColumnFilteringObservationProvider<S, T extends ObservationInterface<S>> implements ObservationProviderInterface<S, T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnFilteringObservationProvider.class);
	private final ObservationProviderInterface<S, T> provider;
	private final Set<Integer> columnsToFilter;
	private final BiFunction<S[], int[], T> factory;
	private final IntFunction<S[]> featureBuilder;

	public ColumnFilteringObservationProvider(ObservationProviderInterface<S, T> provider, Set<? extends Number> columnsToFilter,
			BiFunction<S[], int[], T> valuesColumnsToObservationFactory, IntFunction<S[]> featureBuilder) {
		this.provider = provider;
		this.columnsToFilter = coerceToIntForInterop(columnsToFilter);
		this.factory = valuesColumnsToObservationFactory;
		this.featureBuilder = featureBuilder;
	}

	@Nonnull
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private final LoggingTimer loggingTimer = new LoggingTimer(LOGGER, 100000, "ColumnFilteringObservationProvider processed lines: ");

			private final Iterator<T> iterator = provider.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public T next() {
				T next = iterator.next();
				int[] columnIndexes = Arrays.stream(next.getColumnIndexes())
						.boxed()
						.filter(column -> !columnsToFilter.contains(column))
						.mapToInt(Integer::intValue)
						.toArray();

				S[] filteredFeatures = Arrays.stream(columnIndexes)
						.boxed()
						.map(next::getFeature)
						.toArray(featureBuilder);
				loggingTimer.nextStep();
				return factory.apply(filteredFeatures, columnIndexes);
			}
		};
	}

	@Nonnull
	private Set<Integer> coerceToIntForInterop(Set<? extends Number> columnsToFilter) {
		return columnsToFilter.stream()
				.map(Number::intValue)
				.collect(Collectors.toSet());
	}
}
