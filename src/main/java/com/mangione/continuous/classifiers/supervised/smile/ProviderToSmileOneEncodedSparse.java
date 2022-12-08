package com.mangione.continuous.classifiers.supervised.smile;

import java.util.function.Function;

import com.google.common.collect.Streams;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseObservation;

public class ProviderToSmileOneEncodedSparse<EXEMPLAR extends ExemplarInterface<Integer, Integer>,
		PROVIDER extends ObservationProviderInterface<Integer, EXEMPLAR>> {

	private final int[][] colEncoded;
	private final int numberOfFeatures;
	private final int[] tags;

	public ProviderToSmileOneEncodedSparse(PROVIDER provider, Function<EXEMPLAR, Integer> tagFunction) {
		numberOfFeatures = provider.getNumberOfFeatures();
		colEncoded = Streams.stream(provider)
				.map(ExemplarInterface::getColumnIndexes)
				.toArray(int[][]::new);
		tags = Streams.stream(provider)
				.map(tagFunction)
				.mapToInt(Integer::intValue)
				.toArray();
	}

	public int[][] getColEndoced() {
		return colEncoded;
	}

	public int[] getTags() {
		return tags;
	}

	public int getNumberOfFeatures() {
		return numberOfFeatures;
	}
}
