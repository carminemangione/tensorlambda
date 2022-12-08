package com.mangione.continuous.classifiers.unsupervised.smile;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

public class SparseToDenseFromProvider<OBSERVATION extends ObservationInterface<Integer>> {
	private final Set<Integer> singleClassColumns;

	public SparseToDenseFromProvider(ObservationProviderInterface<Integer, OBSERVATION> provider) {
		singleClassColumns = IntStream.range(0, provider.getNumberOfFeatures()).boxed().collect(Collectors.toSet());
		Set<Integer> nonZeroColumns = provider.getStream()
				.map(this::nonZeroColumns)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());
		singleClassColumns.removeAll(nonZeroColumns);
	}

	public int[] process(OBSERVATION obs) {
		return IntStream.range(0, obs.numberOfFeatures())
				.boxed()
				.filter(column->!singleClassColumns.contains(column))
				.map(obs::getFeature)
				.mapToInt(Integer::intValue)
				.toArray();
	}

	private Set<Integer> nonZeroColumns(OBSERVATION observation) {
		return IntStream.range(0, observation.numberOfFeatures())
				.boxed()
				.filter(column -> !observation.getFeature(column).equals(0))
				.collect(Collectors.toSet());
	}
}
