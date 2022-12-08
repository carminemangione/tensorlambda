package com.mangione.continuous.performance;

public class ROCPoint implements ROCPointInterface {

	private final double threshold;
	private final double falseNegativeRate;
	private final double trueNegativeRate;
	private final double falsePositiveRate;
	private final double truePositiveRate;

	ROCPoint(int numNegativeBelowThreshold, int numPositiveBelowThreshold, int totalNegCount, int totalPosCount, double threshold) {
		this.threshold = threshold;
		falseNegativeRate = (double) numPositiveBelowThreshold / (double) totalPosCount;
		trueNegativeRate = (double) numNegativeBelowThreshold / (double) totalNegCount;
		falsePositiveRate = 1.0 - trueNegativeRate;
		truePositiveRate = 1.0 - falseNegativeRate;
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
		return threshold;
	}
}
