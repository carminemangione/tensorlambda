package com.mangione.continuous.calculators;

import com.mangione.continuous.calculators.stats.ColumnStats;
import com.mangione.continuous.calculators.stats.ColumnStatsBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MinMaxScaling implements VariableCalculator<Double, Double>, Serializable {
	private static final long serialVersionUID = -5847082838258864601L;
	private final ColumnStats stats;

	public static Map<Integer, VariableCalculator<Double, Double>> toIndexedMap(ColumnStatsBuilder columnStatsBuilder) {
		AtomicInteger index = new AtomicInteger();
		return columnStatsBuilder.getStats()
				.stream()
				.collect(Collectors.toMap(columnStats -> index.getAndIncrement(), MinMaxScaling::new));
	}


	MinMaxScaling(ColumnStats stats) {
		this.stats = stats;
	}

	VariableCalculator<Double, Double> getInvertedCalculator() {
		return new MinMaxInverter(stats);
	}

	@Override
	public List<Double> apply(Double valueToScale) {
		return Collections.singletonList((valueToScale - stats.min()) / (stats.max() - stats.min()));
	}
}
