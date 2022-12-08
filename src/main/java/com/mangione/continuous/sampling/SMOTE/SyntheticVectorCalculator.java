package com.mangione.continuous.sampling.SMOTE;

import static com.mangione.continuous.util.coersion.CoerceToIntArray.coerce;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.mangione.continuous.calculators.SparseHammingDistance;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.sparse.SparseExemplarFactoryInterface;
import com.mangione.continuous.util.coersion.CoerceToIntArray;

public class SyntheticVectorCalculator<EXEMPLAR extends ExemplarInterface<Integer, Integer>> {
	private final SparseExemplarFactoryInterface<Integer, EXEMPLAR> exemplarFactory;
	private final Random random;
	private final SparseHammingDistance<EXEMPLAR> hammingDistance;


	public SyntheticVectorCalculator(Function<EXEMPLAR, Integer> tagFunction,
			SparseExemplarFactoryInterface<Integer, EXEMPLAR> exemplarFactory, Random random) {
		this.exemplarFactory = exemplarFactory;
		this.random = random;
		this.hammingDistance = new SparseHammingDistance<>();
	}

	public EXEMPLAR generateVector(EXEMPLAR first, EXEMPLAR second) {
		int[] syntheticColumns = generateSyntheticColumnsToSet(first, second);
		return exemplarFactory.create(createAllValuesOnes(syntheticColumns),
				syntheticColumns, first.numberOfFeatures(), second);
	}

	@Nonnull
	private static Integer[] createAllValuesOnes(int[] syntheticColumns) {
		return Arrays.stream(syntheticColumns)
				.boxed()
				.mapToInt(x -> 1)
				.boxed()
				.toArray(Integer[]::new);
	}

	@Nonnull
	private int[] generateSyntheticColumnsToSet(EXEMPLAR first, EXEMPLAR second) {
		int distance = hammingDistance.calculateDistance(first, second);
		if (distance == 0)
			return new int[0];
		int randomDistanceFromFirst = random.nextInt(distance);
		Set<Integer> intersection = getIntersection(first, second);
		Set<Integer> disjointIndexes = new HashSet<>(Arrays.asList(coerce(first.getColumnIndexes())));
		disjointIndexes.addAll(Arrays.asList(CoerceToIntArray.coerce(second.getColumnIndexes())));

		disjointIndexes.removeAll(intersection);

		return createSyntheticBuyMovingFromFirstToSecond(first, second, new LinkedList<>(disjointIndexes), randomDistanceFromFirst, random);
	}

	@Nonnull
	private Set<Integer> getIntersection(EXEMPLAR first, EXEMPLAR second) {
		Set<Integer> intersection = new HashSet<>(Arrays.asList(coerce(first.getColumnIndexes())));
		intersection.retainAll(Arrays.asList(coerce(second.getColumnIndexes())));
		intersection = intersection.stream()
				.filter(index -> first.getFeature(index).equals(second.getFeature(index))).collect(Collectors.toSet());
		return intersection;
	}

	@Nonnull
	private int[] createSyntheticBuyMovingFromFirstToSecond(
			EXEMPLAR first, EXEMPLAR second, List<Integer> disjointColumnIndexes, int targetDistanceFromFirst, Random random) {
		Set<Integer> syntheticColumns = new HashSet<>(Arrays.asList(coerce(first.getColumnIndexes())));
		for (int i = 0; i < targetDistanceFromFirst; i++) {
			moveRandomlyTowardSecondVector(second, disjointColumnIndexes, syntheticColumns, random);
		}
		return syntheticColumns.stream().sorted().mapToInt(Integer::intValue).toArray();
	}

	private void moveRandomlyTowardSecondVector(EXEMPLAR second, List<Integer> disjointColumnIndexes, Set<Integer> syntheticColumns, Random random) {
		int indexOfChosenValue = random.nextInt(disjointColumnIndexes.size());
		int featureToFile = disjointColumnIndexes.get(indexOfChosenValue);
		moveCloserForFeature(second, syntheticColumns, featureToFile);
		disjointColumnIndexes.remove(indexOfChosenValue);
	}

	private void moveCloserForFeature(EXEMPLAR second, Set<Integer> syntheticColumns, int featureToFlip) {
		if (second.getFeature(featureToFlip) == 0)
			syntheticColumns.remove(featureToFlip);
		else
			syntheticColumns.add(featureToFlip);
	}

}
