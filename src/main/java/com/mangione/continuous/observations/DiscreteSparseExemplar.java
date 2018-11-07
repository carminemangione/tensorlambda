package com.mangione.continuous.observations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DiscreteSparseExemplar implements ExemplarInterface<Integer, Integer> {

	private final int[] values;
	private final int[] columns;
	private final int numberOfColumns;

	public DiscreteSparseExemplar(int[] values, int[] columns, int numberOfColumns) {
		if (values.length != columns.length + 1)
			throw new IllegalArgumentException("Length of values must be one greater that columns for target value");
		if (numberOfColumns < 1)
			throw new IllegalArgumentException("Number of columns must be greater than 0");
		this.values = values;
		this.columns = columns;
		this.numberOfColumns = numberOfColumns;
	}

	@Override
	public Integer getTarget() {
		return values[values.length - 1];
	}

	@Override
	public int getTargetIndex() {
		return featureLength();
	}

	@Override
	public List<Integer> getFeatures() {
		List<Integer> features = new ArrayList<>(generateZeroFeatures());
		IntStream.range(0, columns.length)
				.forEach(i -> features.set(columns[i], values[i]));
		return features;
	}

	private List<Integer> generateZeroFeatures() {
		return Stream.generate(() -> 0)
				.limit(featureLength())
				.collect(Collectors.toList());
	}

	private int featureLength() {
		return numberOfColumns - 1;
	}

	@Override
	public List<Integer> getAllColumns() {
		List<Integer> features = getFeatures();
		features.add(getTarget());
		return features;
	}
}
