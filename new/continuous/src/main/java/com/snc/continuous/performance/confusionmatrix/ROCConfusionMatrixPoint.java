package com.mangione.continuous.performance.confusionmatrix;

import com.mangione.continuous.performance.ROCPointInterface;

public class ROCConfusionMatrixPoint implements ROCPointInterface {
	private final double truePositiveRate;
	private final double trueNegativeRate;
	private final double falsePositiveRate;
	private final double falseNegativeRate;
	private final double score;

	public ROCConfusionMatrixPoint(ConfusionMatrix confusionMatrix) {
		truePositiveRate = (double) confusionMatrix.getNumTruePositive() /
				(confusionMatrix.getNumTruePositive() + confusionMatrix.getNumFalseNegative());
		trueNegativeRate = (double) confusionMatrix.getNumTrueNegative() /
				(confusionMatrix.getNumTrueNegative() + confusionMatrix.getNumFalsePositive());
		falsePositiveRate = 1 - trueNegativeRate;
		falseNegativeRate = 1 - truePositiveRate;
		this.score = confusionMatrix.getThreshold();

	}

	@Override
	public double getFalsePositiveRate() {
		return falsePositiveRate;
	}

	@Override
	public double getTruePositiveRate() {
		return truePositiveRate;
	}

	@Override
	public double getTrueNegativeRate() {
		return trueNegativeRate;
	}

	@Override
	public double getFalseNegativeRate() {
		return falseNegativeRate;
	}

	@Override
	public double getThreshold() {
		return score;
	}
}
