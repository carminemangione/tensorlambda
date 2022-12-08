package com.mangione.continuous.sampling.reservoir;

import java.util.Map;

import org.junit.Test;

public class ReservoirSamplerWithoutReplacementTest extends ReservoirSamplerWithoutReplacementTestParent {
	@Test
	public void largeSampleVerifyProbabilities() {
		int totalNumberOfRecords = 100000;
		int sampleSize = 1000;
		double probSuccess = 0.5;

		double[] classProbabilities = {1.0 - probSuccess, probSuccess};
		Map<Integer, Integer> classificationCounts = generateDataAndSample(ReservoirSamplerWithoutReplacement::new,
				totalNumberOfRecords, sampleSize, classProbabilities);
		int successes = classificationCounts.get(1);
		validateResults(sampleSize, probSuccess, successes);
	}
}