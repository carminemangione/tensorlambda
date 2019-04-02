package com.mangione.continuous.observations.sparse;

import com.mangione.continuous.observations.ExemplarInterface;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class FailedTestExemplar implements ExemplarInterface<Integer, Integer>, SparseObservationInterface<Integer> {

	private final boolean fFailed;
	private final int fTrack;
	private final SparseExemplar<Integer> fSparseExemplar;

	public FailedTestExemplar(String nextLine, int numFeatures) {
		String[] featureStrings = nextLine.split(" ");
		if (featureStrings.length < 3)
			throw new IllegalArgumentException("Must supply at least target status and trackname");
		int numberOfNonZeroFeatures = featureStrings.length - 3;

		int target = Integer.parseInt(featureStrings[numberOfNonZeroFeatures]);
		int testStatus = Integer.parseInt(featureStrings[numberOfNonZeroFeatures + 1]);
		if (testStatus != 0 && testStatus != 1)
			throw new IllegalArgumentException("test status invalid, must be 0 or 1. Value: " + testStatus);
		fFailed = testStatus == 1;
		fTrack = Integer.parseInt(featureStrings[numberOfNonZeroFeatures + 2]);

		fSparseExemplar = createSparseExemplar(numFeatures, featureStrings, numberOfNonZeroFeatures, target);
	}

	public boolean isFailed() {
		return fFailed;
	}

	public Integer getTrack() {
		return fTrack;
	}

	@Override
	public Integer getTarget() {
		return fSparseExemplar.getTarget();
	}

	@Override
	public int getTargetIndex() {
		return fSparseExemplar.getTargetIndex();
	}

	@Override
	public void setFeature(int index, Integer value) {
	}

	@Override
	public List<Integer> getFeatures() {
		return fSparseExemplar.getAllColumns();
	}

	@Override
	public List<Integer> getAllColumns() {
		return fSparseExemplar.getAllColumns();
	}

	@Override
	public List<Integer> getColumnIndexes() {
		return fSparseExemplar.getColumnIndexes();
	}

	private SparseExemplar<Integer> createSparseExemplar(int numFeatures, String[] featureStrings,
			int numberOfNonZeroFeatures, int target) {
		Integer[] featureAndTargetColumns = createFeatureColumnsAddingTargetAsLast(featureStrings, numFeatures,
				numberOfNonZeroFeatures).toArray(new Integer[0]);

		Integer[] featureAndTargetValues = createFeatureValuesAddingTargetValueAsLast(numberOfNonZeroFeatures, target);
		return new SparseExemplar<>(featureAndTargetValues, featureAndTargetColumns,
				numFeatures + 1, 0, numFeatures);
	}

	@Override
	public int numberOfFeatures() {
		return fSparseExemplar.numberOfFeatures();
	}

	private List<Integer> createFeatureColumnsAddingTargetAsLast(String[] featureStrings, int numFeatures, int numberOfNonZeroFeatures) {
		List<Integer> featureColumnList = Arrays.stream(Arrays.copyOfRange(featureStrings, 0, numberOfNonZeroFeatures))
				.map(Integer::parseInt)
				.collect(Collectors.toList());

		featureColumnList.add(numFeatures);
		return featureColumnList;
	}

	@Nonnull
	private Integer[] createFeatureValuesAddingTargetValueAsLast(int numberOfNonZeroFeatures, int target) {
		Integer[] featureAndTargetValues = new Integer[numberOfNonZeroFeatures + 1];
		Arrays.fill(featureAndTargetValues, 1);
		featureAndTargetValues[numberOfNonZeroFeatures] = target;
		return featureAndTargetValues;
	}
}
