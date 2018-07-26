package com.mangione.continuous.calculators;

import com.mangione.continuous.calculators.stats.ColumnStats;

import java.util.Collections;
import java.util.List;

public class MinMaxInverter implements VariableCalculator<Double, Double> {
	private final ColumnStats stats;

	public MinMaxInverter(ColumnStats stats) {
		this.stats = stats;
	}
	@Override
	public List<Double> apply(Double feature, List<Double> features) {
		return Collections.singletonList(feature * (stats.max() - stats.min()) + stats.min());
	}
}
