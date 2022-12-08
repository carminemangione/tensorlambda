package com.mangione.continuous.sampling.SMOTE;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.mangione.continuous.observations.sparse.SparseExemplar;

class RandomDiscreteExemplarGenerator {
	private final Random random;
	private double fillRatio;

	RandomDiscreteExemplarGenerator(Random random) {
		this.random = random;
	}

	@Nonnull
	List<SparseExemplar<Integer, Integer>> createExemplars(int numFeatures, int numExemplars) {
		return createExemplars(numFeatures, 0, numExemplars, 1.0);
	}

	@Nonnull
	List<SparseExemplar<Integer, Integer>> createExemplars(int numFeatures, int tag, int numExemplars, double fillRatio) {
		this.fillRatio = fillRatio;
		return IntStream.range(0, numExemplars)
				.boxed()
				.map(i -> genDiscreteExemplar(numFeatures, tag))
				.collect(Collectors.toList());
	}
	private SparseExemplar<Integer, Integer> genDiscreteExemplar(int numFeatures, int tag) {
		int numSet = (int) Math.round(numFeatures * random.nextDouble() * fillRatio);
		int[] columns = IntStream.range(0, numSet)
				.boxed()
				.mapToInt(x -> random.nextInt(numFeatures))
				.distinct()
				.sorted()
				.toArray();
		Integer[] values = Arrays.stream(columns).boxed().mapToInt(i -> 1).boxed().toArray(Integer[]::new);
		return new SparseExemplar<>(values, columns, numFeatures, 0, tag);
	}
}
