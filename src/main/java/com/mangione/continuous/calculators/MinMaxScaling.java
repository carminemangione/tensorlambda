package com.mangione.continuous.calculators;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.mangione.continuous.calculators.stats.ColumnStats;
import com.mangione.continuous.observationproviders.VariableCalculator;

public class MinMaxScaling implements VariableCalculator<Double, Double>, Serializable {
	private static final long serialVersionUID = -5847082838258864601L;
	private final ColumnStats stats;

	public MinMaxScaling(ColumnStats stats) {

		this.stats = stats;
	}

	@Override
	public List<Double> apply(Double valueToScale) {
		return Collections.singletonList(valueToScale - stats.min() / (stats.max() - stats.min()));
	}
}
