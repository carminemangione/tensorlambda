package com.mangione.continuous.calculators;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.mangione.continuous.calculators.stats.ColumnStats;
import com.mangione.continuous.calculators.stats.ColumnStatsBuilder;

public class MinMaxScaling implements VariableCalculator<Double, Double>, Serializable {
	private static final long serialVersionUID = -5847082838258864601L;
	private final ColumnStats stats;

	public static Map<Integer, VariableCalculator<Double, Double>> toIndexedMap(ColumnStatsBuilder columnStatsBuilder) {
		AtomicInteger index = new AtomicInteger();
		return columnStatsBuilder.getStats()
				.stream()
				.collect(Collectors.toMap(columnStats -> index.getAndIncrement(), MinMaxScaling::new));
	}


	public MinMaxScaling(ColumnStats stats) {
		this.stats = stats;
	}

	@Override
	public List<Double> apply(Double valueToScale) {
		return Collections.singletonList(valueToScale - stats.min() / (stats.max() - stats.min()));
	}
}
