package com.mangione.continuous.calculators.stats;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AllColumnsStats {

	private final List<ColumnStats.Builder> statsBuilders;

	AllColumnsStats(int numberOfColumns) {
		statsBuilders = IntStream.range(0, numberOfColumns)
				.mapToObj(x -> new ColumnStats.Builder(20))
				.collect(Collectors.toList());
	}

	public void add(List<? extends Number> features) {
		if (features.size() != statsBuilders.size())
			throw new IllegalArgumentException(String.format("Expected %d features but got %d",
					statsBuilders.size(), features.size()));
		IntStream.range(0, statsBuilders.size())
				.forEach(index -> statsBuilders.get(index).add(features.get(index).doubleValue()));
	}

	List<ColumnStats> getColumnStats() {
		return statsBuilders.stream()
				.map(ColumnStats.Builder::build)
				.collect(Collectors.toList());
	}
}
