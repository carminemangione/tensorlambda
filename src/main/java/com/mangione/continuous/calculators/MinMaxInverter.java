package com.mangione.continuous.calculators;

import com.mangione.continuous.calculators.stats.ColumnStats;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MinMaxInverter implements Function<Double, List<Double>> {
	private final ColumnStats stats;

	public MinMaxInverter(ColumnStats stats) {
		this.stats = stats;
	}

	@Override
	public List<Double> apply(Double feature) {
		return Collections.singletonList(feature * (stats.max() - stats.min()) + stats.min());
	}
}
