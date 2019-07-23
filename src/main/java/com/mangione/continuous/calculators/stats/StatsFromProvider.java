package com.mangione.continuous.calculators.stats;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class StatsFromProvider {
	private final List<ColumnStats> columnStats;

	public StatsFromProvider(ObservationProviderInterface<Double, ObservationInterface<Double>> provider, int numberOfBins) {
		List<ColumnStats.Builder> builders = initializeBuilders(provider, numberOfBins);
		this.columnStats = calculateColumnStats(provider, builders);
	}

	@NotNull
	private List<ColumnStats> calculateColumnStats(ObservationProviderInterface<Double, ObservationInterface<Double>> provider, List<ColumnStats.Builder> builders) {
		for (ObservationInterface<Double> observation : provider) {
			for (int i = 0; i < builders.size(); i++) {
				builders.get(i).add(observation.getFeature(i));
			}
		}

		return builders.stream()
				.map(ColumnStats.Builder::build)
				.collect(Collectors.toList());
	}

	@NotNull
	private List<ColumnStats.Builder> initializeBuilders(ObservationProviderInterface<Double, ObservationInterface<Double>> provider, int numberOfBins) {
		Iterator<ObservationInterface<Double>> iterator = provider.iterator();
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
