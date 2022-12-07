package com.mangione.continuous.thirdparty.apache;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.ArrayUtils;

import Jama.Matrix;
import Jama.QRDecomposition;
import com.mangione.continuous.model.SupervisedModelInterace;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
<<<<<<< HEAD
import com.mangione.continuous.observations.dense.DiscreteExemplar;
=======
>>>>>>> 73d9563 (Migrated file changes from the source.)
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.ContinuousExemplar;

public class LinearRegression implements SupervisedModelInterace<Double, Double, ContinuousExemplar> {
	private static final long serialVersionUID = 5422020901503909935L;


	private  Matrix beta;
	private double SSE;
	private double SST;

	@Override
	public void train(ObservationProviderInterface<Double, ContinuousExemplar> provider) {

		double[][] inputs = new double[provider.size()][];
		double[] targets = new double[provider.size()];
		AtomicInteger index = new AtomicInteger();
		StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(provider.iterator(), Spliterator.ORDERED), false)
				.forEach(exemplar -> {
					inputs[index.get()] = toPrimitiveArray(exemplar.getFeatures(Double[]::new));
					targets[index.getAndIncrement()] = exemplar.getLabel();
				});
		linearRegression(inputs, targets);
	}

	@Override
	public double score(ObservationInterface<Double> observation) {
		double[][] obs = new double[1][];
		obs[0] = ArrayUtils.toPrimitive(observation.getFeatures(Double[]::new));
		Matrix matrix = new Matrix(obs);
		Matrix estimate = matrix.times(beta);

		return estimate.get(0, 0);
	}

	private double[] toPrimitiveArray(Double[] doubleList) {
		double[] doubles = new double[doubleList.length];
		AtomicInteger index = new AtomicInteger();
		Arrays.stream(doubleList).forEach(x -> doubles[index.getAndIncrement()] = x);
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
