package com.mangione.continuous.model.modelproviders;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ObservationInterface;

public class DoubleUnsupervisedModelProvider{
	private final double[][] inputArray;


	public DoubleUnsupervisedModelProvider(ObservationProvider<? extends Number,
			? extends ObservationInterface<? extends Number>> observationProvider) {
		inputArray = buildDoubleArrayFromProvider(observationProvider);
	}

	private <T> Iterable<T> iteratorToIterable(Iterator<T> iterator) {
		return () -> iterator;
	}

	private <T> Stream<T> iterableToStream(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	public double[] get(int row) {
		return inputArray[row];
	}

	public int getNumberOfLines() {
		return inputArray.length;
	}

	private double[][] buildDoubleArrayFromProvider(ObservationProvider<? extends Number,
			? extends ObservationInterface<? extends Number>> observationProvider) {
		Stream<double[]> stream = iterableToStream(iteratorToIterable(observationProvider.iterator()))
				.map(ObservationInterface::getFeatures)
				.map(features -> features.stream().mapToDouble(Number::doubleValue).toArray());
		return stream.toArray(double[][]::new);
	}

	public Iterator<double[]> iterator() {
		return Arrays.stream(inputArray).iterator();
	}
}
