package com.mangione.continuous.performance.confusionmatrix;

public class ConfusionMatrix {
	private final int numTruePositive;
	private final int numFalsePositive;
	private final int numTrueNegative;
	private final int numFalseNegative;
	private final double threshold;

	public ConfusionMatrix(int numTruePositive, int numFalsePositive, int numTrueNegative, int numFalseNegative, double threshold) {
		this.numTruePositive = numTruePositive;
		this.numFalsePositive = numFalsePositive;
		this.numTrueNegative = numTrueNegative;
		this.numFalseNegative = numFalseNegative;
		this.threshold = threshold;
	}

	public int getNumTruePositive() {
		return numTruePositive;
	}

	public int getNumFalsePositive() {
		return numFalsePositive;
	}

	public int getNumTrueNegative() {
		return numTrueNegative;
	}

	public int getNumFalseNegative() {
		return numFalseNegative;
	}

	public double getThreshold() {
		return threshold;
	}

	public static class Builder {
		private final double threshold;
		private int numTruePositive;
		private int numFalsePositive;
		private int numTrueNegative;
		private int numFalseNegative;

		public Builder(double threshold) {
			this.threshold = threshold;
		}
		
		public Builder add(int predicted, int actual) {
			if (predicted == 1) {
				if (actual == 1)
					numTruePositive++;
				else
					numFalsePositive++;
			}
			if (predicted == 0) {
				if (actual == 0)
					numTrueNegative++;
				else
					numFalseNegative++;
			}
			return this;
		}

		public ConfusionMatrix build() {
			return new ConfusionMatrix(numTruePositive, numFalsePositive, numTrueNegative, numFalseNegative, threshold);
		}
	}
}
