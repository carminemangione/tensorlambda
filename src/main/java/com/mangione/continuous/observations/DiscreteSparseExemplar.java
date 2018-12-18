package com.mangione.continuous.observations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscreteSparseExemplar implements ExemplarInterface<Integer, Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscreteSparseExemplar.class);
	private final int[] values;
	private final int[] columns;
	private final int numberOfColumns;
	private final int target;

	public DiscreteSparseExemplar(int[] values, int[] columns, int numberOfColumns, int target) {
		if (values.length != columns.length)
			throw new IllegalArgumentException("Length of values must equal number of columns");
		this.values = values;
		this.columns = columns;
		this.numberOfColumns = numberOfColumns;
		this.target =  target;
	}

	DiscreteSparseExemplar(int[] values, int[] columns, int numberOfColumns) {
		if (values.length != columns.length + 1)
			throw new IllegalArgumentException("Length of values must be one greater that columns for target value");
		if (numberOfColumns < 1)
			throw new IllegalArgumentException("Number of columns must be greater than 0");
		this.values = values;
		this.columns = columns;
		this.numberOfColumns = numberOfColumns;
		this.target =  values[values.length - 1];
		IntStream.range(0, columns.length)
				.filter(i -> columns[i] >= numberOfColumns)
				.forEach(x -> LOGGER.warn("Index out of bounds: " + x));

	}

	@Override
	public Integer getTarget() {
		return target;
	}

	@Override
	public int getTargetIndex() {
		return featureLength();
	}

	@Override
	public List<Integer> getFeatures() {
		List<Integer> features = new ArrayList<>(generateZeroFeatures());
		IntStream.range(0, columns.length)
				.filter(i -> columns[i] < numberOfColumns)
				.forEach(i -> features.set(columns[i], values[i]));
		return features;
	}

	@Override
	public List<Integer> getAllColumns() {
		List<Integer> features = getFeatures();
		features.add(getTarget());
		return features;
	}

	private List<Integer> generateZeroFeatures() {
		return Stream.generate(() -> 0)
				.limit(numberOfColumns)
				.collect(Collectors.toList());
	}

	private int featureLength() {
		return numberOfColumns - 1;
	}
}
