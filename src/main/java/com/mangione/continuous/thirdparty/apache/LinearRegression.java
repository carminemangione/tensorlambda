package com.mangione.continuous.thirdparty.apache;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.ArrayUtils;

import Jama.Matrix;
import Jama.QRDecomposition;
import com.mangione.continuous.model.SupervisedModelInterace;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.ObservationInterface;

public class LinearRegression implements SupervisedModelInterace<Double, Integer, DiscreteExemplar<Double>> {
	private static final long serialVersionUID = 5422020901503909935L;


	private  Matrix beta;
	private double SSE;
	private double SST;

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
		linearRegression(inputs, targets);
	}

	@Override
	public double score(ObservationInterface<Double> observation) {
		double[][] obs = new double[1][];
		obs[0] = ArrayUtils.toPrimitive(observation.getFeatures().toArray(new Double[observation.getFeatures().size()]));
		Matrix matrix = new Matrix(obs);
		Matrix estimate = matrix.times(beta);

		return estimate.get(0, 0);
	}

	private double[] toPrimitiveArray(List<Double> doubleList) {
		double[] doubles = new double[doubleList.size()];
		AtomicInteger index = new AtomicInteger();
		doubleList.forEach(x -> doubles[index.getAndIncrement()] = x);
		return doubles;
	}

	private void linearRegression(double[][] x, double[] y) {
		int numberOfRows = y.length;

		Matrix X = new Matrix(x);
		Matrix Y = new Matrix(y, numberOfRows);

		QRDecomposition qr = new QRDecomposition(X);
		beta = qr.solve(Y);

		double sum = 0.0;
		for (double aY : y) sum += aY;


		double mean = sum / numberOfRows;

		// total variation to be accounted for
		for (double aY : y) {
			double dev = aY - mean;
			SST += dev * dev;
		}

		// variation not accounted for
		Matrix residuals = X.times(beta).minus(Y);
		SSE = residuals.norm2() * residuals.norm2();
	}

	public double beta(int j) {
		return beta.get(j, 0);
	}

	public double R2() {
		return 1.0 - SSE/SST;
	}
}
