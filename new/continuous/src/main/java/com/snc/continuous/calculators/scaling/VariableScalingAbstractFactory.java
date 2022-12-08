package com.mangione.continuous.calculators.scaling;

import com.mangione.continuous.calculators.stats.ColumnStats;

public interface VariableScalingAbstractFactory<R extends Number, S extends Number> {
	VariableScalingInterface<R, S> createScaling(ColumnStats columnStats);
}
