package com.mangione.continuous.thirdparty.apache;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import com.mangione.continuous.model.SupervisedModelInterace;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.ObservationInterface;

public class LinearRegression implements SupervisedModelInterace<Double, Integer, DiscreteExemplar<Double>> {
	private static final long serialVersionUID = 5422020901503909935L;
	private double[] coe;
	private double RSquared;

	@Override
	public void train(ObservationProviderInterface<Double, DiscreteExemplar<Double>> provider) {

		double[][] inputs = new double[(int) provider.getNumberOfLines()][];
		double[] targets = new double[(int) provider.getNumberOfLines()];
		AtomicInteger index = new AtomicInteger();
		StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(provider.iterator(), Spliterator.ORDERED), false)
				.forEach(exemplar -> {
					inputs[index.get()] = toPrimitiveArray(exemplar.getFeatures());
					targets[index.getAndIncrement()] = exemplar.getContinuousValue();
				});
		OLSMultipleLinearRegression linearRegression = new OLSMultipleLinearRegression();
		linearRegression.newSampleData(targets, inputs);
		coe = linearRegression.estimateRegressionParameters();
		RSquared = linearRegression.calculateRSquared();

	}

	@Override
	public double score(ObservationInterface<Double> observation) {
		double result = 0;
		for (int i = 0; i < coe.length; i++)
			result +=  coe[i] * Math.pow(observation.getFeatures().get(i), i);
		return result;
	}

	private double[] toPrimitiveArray(List<Double> doubleList) {
		double[] doubles = new double[doubleList.size()];
		AtomicInteger index = new AtomicInteger();
		doubleList.forEach(x -> doubles[index.getAndIncrement()] = x);
		return doubles;
	}

	public double getRSquared() {
		return RSquared;
	}
}
