package com.mangione.continuous.sampling.SMOTE;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.calculators.SparseHammingDistance;
import com.mangione.continuous.observations.sparse.SparseExemplar;

public class SyntheticVectorCalculatorTest {
	private Random random;
	private SyntheticVectorCalculator<SparseExemplar<Integer, Integer>> calculator;
	private SparseHammingDistance<SparseExemplar<Integer, Integer>> hammingDistance;

	@Before
	public void setUp() {
		random = new Random(383767);
		calculator = new SyntheticVectorCalculator<>(SparseExemplar::getLabel,
				(features, columns, numberOfColumns, original) ->
						new SparseExemplar<>(features, columns, numberOfColumns, 0, original.getLabel()),random);
		hammingDistance = new SparseHammingDistance<>();
	}

	@Test
	public void twoVectorsKnownDistanceNoOverlap() {
		SparseExemplar<Integer, Integer> first = new SparseExemplar<>(new Integer[]{1,1,1,1}, new int[]{0,1,2,3}, 8, 0, 1);
		SparseExemplar<Integer, Integer> second = new SparseExemplar<>(new Integer[]{1,1,1,1}, new int[]{4,5,6,7}, 8, 0, 1);
		assertEquals(8, hammingDistance.calculateDistance(first, second));
		SparseExemplar<Integer, Integer> synthetic = calculator.generateVector(first, second);
		validateDistances(first, second, synthetic);
	}

	@Test
	public void twoVectorsKnownDistanceOverlap() {
		SparseExemplar<Integer, Integer> first = new SparseExemplar<>(new Integer[]{1,1,1,1}, new int[]{0,1,2,3}, 8, 0, 1);
		SparseExemplar<Integer, Integer> second = new SparseExemplar<>(new Integer[]{1,1,1,1}, new int[]{2,3,6,7}, 8, 0, 1);
		assertEquals(4, hammingDistance.calculateDistance(first, second));
		SparseExemplar<Integer, Integer> synthetic = calculator.generateVector(first, second);
		validateDistances(first, second, synthetic);
	}

	@Test
	public void emptyVectorsNoOp() {
		SparseExemplar<Integer, Integer> first = new SparseExemplar<>(new Integer[0], new int[0], 8, 0, 1);
		SparseExemplar<Integer, Integer> second = new SparseExemplar<>(new Integer[0], new int[0], 8, 0, 1);
		assertEquals(0, hammingDistance.calculateDistance(first, second));
		SparseExemplar<Integer, Integer> synthetic = calculator.generateVector(first, second);
		validateDistances(first, second, synthetic);
	}

	@Test
	public void distanceAlwaysSumsToOriginal() {
		List<SparseExemplar<Integer, Integer>> exemplars =
				new RandomDiscreteExemplarGenerator(random).createExemplars(50, 1000);
		for (int i = 0; i < exemplars.size() - 1; i++) {
			SparseExemplar<Integer, Integer> first = exemplars.get(i);
			SparseExemplar<Integer, Integer> second = exemplars.get(i + 1);
			try {
				SparseExemplar<Integer, Integer> synthetic = calculator.generateVector(first, second);
				validateDistances(first, second, synthetic);
			} catch (Exception e) {
				System.out.println(Arrays.toString(first.getColumnIndexes()));
				System.out.println(Arrays.toString(second.getColumnIndexes()));
				System.out.println(i);
			}
		}
	}

	private void validateDistances(SparseExemplar<Integer, Integer> first, SparseExemplar<Integer, Integer> second, SparseExemplar<Integer, Integer> synthetic) {
		long distanceFirstToSecond = hammingDistance.calculateDistance(first, second);
		long distanceToFirst = hammingDistance.calculateDistance(first, synthetic);
		long distanceToSecond = hammingDistance.calculateDistance(second, synthetic);
		assertEquals(distanceFirstToSecond, distanceToFirst + distanceToSecond);
	}
}