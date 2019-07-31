package com.mangione.continuous.calculators.stats;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observationproviders.RowFilteringObservationProvider;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

public class GroupedStatsFromProvider {

	private final Map<Object, List<ColumnStats>> columnStats;

	@SuppressWarnings("WeakerAccess")
	public GroupedStatsFromProvider(ObservationProviderInterface<Object, ObservationInterface<Object>> provider, int groupByColumn) {
		Map<Object, ColumnStatsBuilder<Double, ObservationProviderInterface<Double, ObservationInterface<Double>>>> nameToColumnStats = new HashMap<>();


		for (ObservationInterface<Object> observation : provider) {
			nameToColumnStats.computeIfAbsent(observation.getFeature(groupByColumn),
					key-> new ColumnStatsBuilder<>(new RemoveGroupAndMakeDoubles(provider, key, groupByColumn)));
		}

		columnStats = nameToColumnStats
				.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().getStats()));
	}

	public Map<Object, List<ColumnStats>> getStats() {
		return columnStats;
	}

	private class RemoveGroupAndMakeDoubles implements ObservationProviderInterface<Double, ObservationInterface<Double>> {

		private final ObservationProviderInterface<Object, ObservationInterface<Object>> provider;
		private final int groupByColumn;

		private RemoveGroupAndMakeDoubles(ObservationProviderInterface<Object, ObservationInterface<Object>> provider,
				Object filterObject, int groupByColumn) {
			this.groupByColumn = groupByColumn;
			this.provider = new RowFilteringObservationProvider<>(provider,
					observationInterface -> !observationInterface.getFeature(groupByColumn).equals(filterObject));
		}

		@Override
		@Nonnull
		public Iterator<ObservationInterface<Double>> iterator() {
			return new DoubleConvertingIterator(provider.iterator());
		}

		@Override
		public Spliterator<ObservationInterface<Double>> spliterator() {
			throw new UnsupportedOperationException("Not implemented yet");
		}


		private class DoubleConvertingIterator implements Iterator<ObservationInterface<Double>> {

			private final Iterator<ObservationInterface<Object>> iterator;

			private DoubleConvertingIterator(Iterator<ObservationInterface<Object>> iterator) {
				this.iterator = iterator;
			}

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public ObservationInterface<Double> next() {
				return createObservation(iterator.next());
			}
		}

		private ObservationInterface<Double> createObservation(ObservationInterface<Object> observation) {
			List<Double> translatedFeatures = IntStream.range(0, observation.getFeatures().size())
					.boxed()
					.filter(index -> index != groupByColumn)
					.map(index -> (Double) observation.getFeature(index))
					.collect(Collectors.toList());

			return new Observation<>(translatedFeatures);
		}
	}
}