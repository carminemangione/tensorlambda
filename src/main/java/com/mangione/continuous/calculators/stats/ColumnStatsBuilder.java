package com.mangione.continuous.calculators.stats;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class ColumnStatsBuilder<R extends Number,
		S extends ObservationProviderInterface<R, ? extends ObservationInterface<R>>> implements Serializable {

	private static final long serialVersionUID = 2584705314151370296L;
	private final List<ColumnStats> columnStats;

	public ColumnStatsBuilder(S provider) {
		Iterator<? extends ObservationInterface<R>> iterator = provider.iterator();
		if (!iterator.hasNext())
			throw new IllegalArgumentException();

		columnStats = calculateColumnStats(provider);

	}

	public ColumnStats get(int value) {
		return columnStats.get(value);
	}

	public List<ColumnStats> getStats() {
		return Collections.unmodifiableList(columnStats);
	}

	private List<ColumnStats> calculateColumnStats(S provider) {
		Iterator<? extends ObservationInterface<R>> iterator = provider.iterator();

		if (!iterator.hasNext())
			throw new RuntimeException("the provider is empty can not count columns");
<<<<<<< HEAD
		int numberOfColumns = iterator.next().getAllColumns().size();

		AllColumnsStats allColumnsStats = new AllColumnsStats(numberOfColumns);
		provider.forEach(observation->allColumnsStats.add(observation.getFeatures()));
=======

		AllColumnsStats allColumnsStats = new AllColumnsStats(provider.getNumberOfFeatures());
		//noinspection Convert2MethodRef - For some reason it won't compile...
		provider.forEach(observation->allColumnsStats.add(
				IntStream.range(0, observation.numberOfFeatures())
				.boxed()
				.map(index->observation.getFeature(index))
				.collect(Collectors.toList())));
>>>>>>> 73d9563 (Migrated file changes from the source.)
		return allColumnsStats.getColumnStats();
	}
}
