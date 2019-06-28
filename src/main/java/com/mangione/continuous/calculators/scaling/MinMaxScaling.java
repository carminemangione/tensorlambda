package com.mangione.continuous.calculators.scaling;

import java.io.Serializable;

import com.mangione.continuous.calculators.stats.ColumnStats;

public class MinMaxScaling<R extends Number> implements VariableScalingInterface<R, Double>, Serializable {
	private static final long serialVersionUID = -5847082838258864601L;
	private final ColumnStats stats;

	MinMaxScaling(ColumnStats stats) {
		this.stats = stats;
	}

	@Override
	public Double apply(R valueToScale) {
		return valueToScale.doubleValue() - stats.min() / (stats.max() - stats.min());
	}
}
