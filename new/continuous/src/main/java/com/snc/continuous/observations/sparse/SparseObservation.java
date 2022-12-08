package com.mangione.continuous.observations.sparse;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.mangione.continuous.observations.ObservationInterface;

public class SparseObservation<FEATURE> implements ObservationInterface<FEATURE> {

	private final int[] columns;
	private final FEATURE[] features;
	private final int numberOfColumns;
	private final FEATURE missingValue;

	public SparseObservation(FEATURE[] features, int[] columns, int numberOfColumns, FEATURE missingValue) {
		if (features.length != columns.length)
			throw new IllegalArgumentException(String.format(
					"Length of features must be the same as columns. features:%d, columns:%d", features.length, columns.length));
		this.columns = columns;
		this.features = features;
		this.numberOfColumns = numberOfColumns;
		this.missingValue = missingValue;
	}

	public FEATURE getMissingValue() {
		return missingValue;
	}

	public int[] getColumnIndexes() {
		return columns;
	}

	public FEATURE[] getSparseFeatureValues() {
		return features;
	}

	@Override
	public FEATURE[] getFeatures(IntFunction<FEATURE[]> featureBuilder) {
		return IntStream.range(0, numberOfColumns).boxed().map(this::getFeature).toArray(featureBuilder);
	}

	@Override
	public FEATURE getFeature(Integer index) {
		validateIndex(index);
		return getValue(index);
	}

	@Override
	public int numberOfFeatures() {
		return numberOfColumns;
	}

	private void validateIndex(int index) {
		if (index < 0)
			throw new IllegalArgumentException("Index may not be negative");
		if (index >= numberOfColumns)
			throw new IllegalArgumentException(String.format("Index of value %d exceeds maximum of %d", index, numberOfColumns));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof SparseObservation)) return false;

		SparseObservation<?> that = (SparseObservation<?>) o;

		return new EqualsBuilder()
				.append(numberOfColumns, that.numberOfColumns)
				.append(columns, that.columns)
				.append(features, that.features)
				.append(getMissingValue(), that.getMissingValue())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(columns)
				.append(features)
				.append(numberOfColumns)
				.append(getMissingValue())
				.toHashCode();
	}

	private FEATURE getValue(int col) {
		int index = Arrays.binarySearch(columns, col);
		return index < 0 ? missingValue : features[index];
	}
}
