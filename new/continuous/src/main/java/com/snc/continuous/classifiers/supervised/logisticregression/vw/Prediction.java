package com.mangione.continuous.classifiers.supervised.logisticregression.vw;

public class Prediction {
	private final int predicted;
	private final int actual;

	public Prediction(int predicted, int actual) {
		this.predicted = predicted;
		this.actual = actual;
	}

	public int getPredicted() {
		return predicted;
	}

	public int getActual() {
		return actual;
	}
}
