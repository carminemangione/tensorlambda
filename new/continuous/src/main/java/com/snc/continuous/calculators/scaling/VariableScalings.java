package com.mangione.continuous.calculators.scaling;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mangione.continuous.calculators.stats.ColumnStatsBuilder;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

class VariableScalings<R extends Number, S extends Number,
		T extends ObservationProviderInterface<R, ? extends ObservationInterface<R>>> {

	private final List<VariableScalingInterface<R, S>> variableScalings;

	VariableScalings(VariableScalingAbstractFactory<R, S> factory, T provider) {
		ColumnStatsBuilder<R, T> statsBuilder = new ColumnStatsBuilder<>(provider);
		variableScalings = IntStream.range(0, provider.getNumberOfFeatures())
				.boxed()
				.map(statsBuilder::get)
				.map(factory::createScaling)
				.collect(Collectors.toList());
	}

	List<S> apply(List<R> values) {
		return IntStream.range(0, values.size())
				.boxed()
				.map(index->variableScalings.get(index).apply(values.get(index)))
				.collect(Collectors.toList());
	}
}
