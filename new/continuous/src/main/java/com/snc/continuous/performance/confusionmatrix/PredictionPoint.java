package com.mangione.continuous.performance.confusionmatrix;

import javax.annotation.Nonnull;

public class PredictionPoint implements Comparable<PredictionPoint> {
	private final double threshold;
	private final int actual;
	private final int predicted;

	PredictionPoint(double threshold, int actual, int predicted) {
		this.threshold = threshold;
		this.actual = actual;
		this.predicted = predicted;
	}

	public double getThreshold() {
		return threshold;
	}

	public int getActual() {
		return actual;
	}

	public int getPredicted() {
		return predicted;
	}

	@Override
	public String toString() {
		return "PredictionPoint{" +
				"threshold=" + threshold +
				", actual=" + actual +
				", predicted=" + predicted +
				'}';
	}

	@Override
	public int compareTo(@Nonnull PredictionPoint o) {
		return -Double.compare(threshold, o.threshold);
	}
}
