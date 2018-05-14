package com.mangione.continuous.calculators;

import java.io.Serializable;
import java.util.function.Function;

import com.mangione.continuous.calculators.stats.ColumnStats;

public class MinMaxScaling implements Function<Double, Double>, Serializable {
	private static final long serialVersionUID = -5847082838258864601L;
	private final ColumnStats stats;

	public MinMaxScaling(ColumnStats stats) {

		this.stats = stats;
	}

	@Override
	public Double apply(Double valueToScale) {
		return (valueToScale - stats.min()) / (stats.max() - stats.min());
	}
}
