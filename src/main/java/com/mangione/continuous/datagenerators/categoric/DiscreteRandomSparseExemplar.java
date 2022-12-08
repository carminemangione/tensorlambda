package com.mangione.continuous.datagenerators.categoric;

import java.util.Random;
import java.util.function.IntFunction;

import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplarBuilder;

public class DiscreteRandomSparseExemplar implements ExemplarInterface<Integer, Integer> {

	private final SparseExemplar<Integer, Integer> sparseExemplar;

	public DiscreteRandomSparseExemplar(int numDimensions, int target, Random random) {
		SparseExemplarBuilder<Integer, Integer> builder = new SparseExemplarBuilder<>(numDimensions, 0, target);
		int numValues = random.nextInt(numDimensions);
		for (int i = 0; i < numValues; i++)
			builder.setFeature(random.nextInt(numDimensions), 1);
		sparseExemplar = builder.build(Integer[]::new);
	}

	@SuppressWarnings("unused")
	public DiscreteRandomSparseExemplar(int numDimensions, int target, double fill, Random random) {
		SparseExemplarBuilder<Integer, Integer> builder = new SparseExemplarBuilder<>(numDimensions, 0, target);
		int numValues = random.nextInt((int) (numDimensions * fill));
		for (int i = 0; i < numValues; i++)
			builder.setFeature(random.nextInt(numDimensions), 1);
		sparseExemplar = builder.build(Integer[]::new);
	}

	@Override
	public Integer getLabel() {
		return sparseExemplar.getLabel();
	}

	@Override
	public Integer[] getFeatures(IntFunction<Integer[]> featureBuilder) {
		return sparseExemplar.getFeatures(Integer[]::new);
	}

	@Override
	public Integer getFeature(Integer index) {
		return sparseExemplar.getFeature(index);
	}

	@Override
	public int[] getColumnIndexes() {
		return sparseExemplar.getColumnIndexes();
	}

	@Override
	public int numberOfFeatures() {
		return sparseExemplar.numberOfFeatures();
	}
}
