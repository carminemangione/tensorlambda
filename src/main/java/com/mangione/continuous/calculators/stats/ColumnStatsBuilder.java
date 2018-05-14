package com.mangione.continuous.calculators.stats;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class ColumnStatsBuilder implements Serializable {

	private final List<ColumnStats> columnStats;

	public ColumnStatsBuilder(ObservationProviderInterface<Double, ObservationInterface<Double>> provider) {
		Iterator<ObservationInterface<Double>> iterator = provider.iterator();
		if (!iterator.hasNext())
			throw new IllegalArgumentException();

		columnStats = calculateColumnStats(provider);

	}

	public ColumnStats get(int value) {
		return columnStats.get(value);
	}

	private List<ColumnStats> calculateColumnStats(ObservationProviderInterface<Double, ObservationInterface<Double>> provider) {

		int numberOfColumns = provider.iterator().next().getFeatures().size();

		List<ColumnStats.Builder> builders = IntStream.range(0, numberOfColumns)
				.mapToObj(x -> new ColumnStats.Builder())
				.collect(Collectors.toList());

		provider.forEach(observation -> IntStream.range(0, numberOfColumns)
				.forEach(index -> builders.get(index).add(observation.getFeatures().get(index))));

		return builders.stream().map(builder -> builder.build()).collect(Collectors.toList());
	}
}
