package com.mangione.continuous.model.modelproviders;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DoubleUnsupervisedModelProvider implements DataProvider<double[]> {
	private final double[][] inputArray;


	public DoubleUnsupervisedModelProvider(ObservationProviderInterface<? extends Number,
				? extends ObservationInterface<? extends Number>> observationProvider) {
		inputArray = buildDoubleArrayFromProvider(observationProvider);
	}

	private <T> Iterable<T> iteratorToIterable(Iterator<T> iterator) {
		return () -> iterator;
	}

	private <T> Stream<T> iterableToStream(Iterable<T> iterable) {
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	@Override
	public int getNumberOfLines() {
		return inputArray.length;
	}

	@Override
	public double[] get(int row) {
		return inputArray[row];
	}

	@Override
	public int getLengthOfObservation() {
		return inputArray[0].length;
	}


	private double[][] buildDoubleArrayFromProvider(ObservationProviderInterface<? extends Number,
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
