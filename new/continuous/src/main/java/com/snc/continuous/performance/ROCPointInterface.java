package com.mangione.continuous.performance;

public interface ROCPointInterface {
	double getFalsePositiveRate();

	double getTruePositiveRate();

	double getTrueNegativeRate();

	double getFalseNegativeRate();

	double getThreshold();
}
