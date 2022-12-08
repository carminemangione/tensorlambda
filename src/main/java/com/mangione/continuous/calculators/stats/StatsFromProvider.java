package com.mangione.continuous.calculators.stats;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess")
public class StatsFromProvider {
	private final List<ColumnStats> columnStats;

	public StatsFromProvider(ObservationProviderInterface<Double, ? extends ObservationInterface<Double>> provider, int numberOfBins) {
		List<ColumnStats.Builder> builders = initializeBuilders(provider, numberOfBins);
		this.columnStats = calculateColumnStats(provider, builders);
	}

	@Nonnull
	private List<ColumnStats> calculateColumnStats(ObservationProviderInterface<Double, ? extends ObservationInterface<Double>> provider, List<ColumnStats.Builder> builders) {
		for (ObservationInterface<Double> observation : provider) {
			for (int i = 0; i < builders.size(); i++) {
				builders.get(i).add(observation.getFeature(i));
			}
		}
		return builders.stream()
				.map(ColumnStats.Builder::build)
				.collect(Collectors.toList());
	}

	@Nonnull
	private List<ColumnStats.Builder> initializeBuilders(ObservationProviderInterface<Double, ? extends ObservationInterface<Double>> provider, int numberOfBins) {
		Iterator<? extends ObservationInterface<Double>> iterator = provider.iterator();
		ObservationInterface<Double> next = iterator.next();
		return IntStream.range(0, next.numberOfFeatures())
				.boxed()
				.map(index -> new ColumnStats.Builder(numberOfBins))
				.collect(Collectors.toList());



	}

	public List<ColumnStats> getColumnStats() {
		return columnStats;
	}
}
