package com.mangione.continuous.performance.confusionmatrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class BinnedPredictionPointsTest {
	private Random random;

	@Before
	public void setUp()  {
		random = new Random(93872);
	}

	@Test
	public void breakScoresIntoBins() {
		int numBins = 3;
		List<PredictionPoint> predictionPoints = new ArrayList<>();
		generatePoints(10, .85, .95, 1, 0, predictionPoints, true);
		generatePoints(5, .75, .84, 0, 1, predictionPoints, true);
		generatePoints(6, .65, .74, 1, 1, predictionPoints, true);
		List<List<PredictionPoint>> binnedPredictionPoints = new BinnedPredictionPoints(predictionPoints, numBins).getBinnedPredictionPoints();
		assertEquals(numBins, binnedPredictionPoints.size());

		validateBin(binnedPredictionPoints, .85, .95, 0, 10);
		validateBin(binnedPredictionPoints, .75, .84, 1, 5);
		validateBin(binnedPredictionPoints,   .65, .74, 2, 6);
	}

	@Test
	public void twoBinsTwoThresholds() {
		List<PredictionPoint> predictionPoints = new ArrayList<>();
		generatePoints(10, .85, .95, 1, 0, predictionPoints, true);
		generatePoints(5, .75, .84, 0, 1, predictionPoints, true);

		List<List<PredictionPoint>> binnedPredictionPoints = new BinnedPredictionPoints(predictionPoints, 2)
				.getBinnedPredictionPoints();
		assertEquals(2, binnedPredictionPoints.size());

		validateBin(binnedPredictionPoints, .85, .95, 0, 10);
		validateBin(binnedPredictionPoints, .75, .84, 1, 5);
	}

	@Test
	public void exactValuesCaptured() {
		List<PredictionPoint> predictionPoints = new ArrayList<>();
		generatePoints(10, .85, .95, 1, 0, predictionPoints, false);
		generatePoints(5, .75, .84, 0, 1, predictionPoints, false);

		List<List<PredictionPoint>> binnedPredictionPoints = new BinnedPredictionPoints(predictionPoints, 2)
				.getBinnedPredictionPoints();
		assertEquals(2, binnedPredictionPoints.size());

		validateBin(binnedPredictionPoints, .85, .95, 0, 10);
		validateBin(binnedPredictionPoints, .75, .84, 1, 5);
	}


	@Test
	public void makesCopyOfList() {
		List<PredictionPoint> predictionPoints = new ArrayList<>();
		generatePoints(10, .85, .95, 1, 0, predictionPoints, true);
		double firstThreshold = predictionPoints.get(0).getThreshold();
		List<List<PredictionPoint>> binnedPredictionPoints = new BinnedPredictionPoints(predictionPoints, 1).getBinnedPredictionPoints();
		assertEquals(firstThreshold, predictionPoints.get(0).getThreshold(), Double.MIN_VALUE);
	}
	

	private void validateBin(List<List<PredictionPoint>> binnedPredictionPoints, double min, double max, int index, int size) {
		assertEquals(size, binnedPredictionPoints.get(index).size());
		for (int i = 0; i < size; i++) {
			assertTrue(binnedPredictionPoints.get(index).get(i).getThreshold() >= min && binnedPredictionPoints.get(index).get(i).getThreshold() <= max);
		}
	}

	private void generatePoints(int num, double minThreshold, double maxThreshold, int actual, int predicted,
			List<PredictionPoint> predictionPoints, boolean addNoise) {
		double range = maxThreshold - minThreshold;
		for (int i = 0; i < num; i++) {
			double threshold = addNoise ? random.nextDouble() * range + minThreshold : minThreshold;
			predictionPoints.add(new PredictionPoint(threshold, actual, predicted));
		}
	}
}