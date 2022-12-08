package com.mangione.continuous.performance.confusionmatrix;

import static org.junit.Assert.*;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PredictionPointTest {
	@Test
	public void compareToInverseOrder() {
		PredictionPoint original = new PredictionPoint(.5, 0, 0);
		PredictionPoint same = new PredictionPoint(.5, 0, 0);
		PredictionPoint smaller = new PredictionPoint(.4, 0, 0);
		PredictionPoint bigger = new PredictionPoint(.8, 0, 0);
		assertEquals(0, original.compareTo(same));
		assertEquals(1, original.compareTo(bigger));
		assertEquals(-1, original.compareTo(smaller));
	}

	@Test
	public void sortInverseOrder() {
		List<PredictionPoint> predictionPoints = new ArrayList<>(Arrays.asList(new PredictionPoint(.5, 0, 0), new PredictionPoint(.4, 0, 0)));
		Collections.sort(predictionPoints);
		assertEquals(0.5, predictionPoints.get(0).getThreshold(), Double.MIN_VALUE);
	}
}