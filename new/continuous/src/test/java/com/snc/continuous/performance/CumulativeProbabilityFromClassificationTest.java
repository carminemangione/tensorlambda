package com.mangione.continuous.performance;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mangione.continuous.classifiers.supervised.smile.TagToIndex;
import com.mangione.continuous.performance.CumulativeProbabilityFromClassification;

public class CumulativeProbabilityFromClassificationTest {
	@Test
	public void OneScoreTwoClasses() {
		TagToIndex tagToIndex = new TagToIndex(new int[]{67, 4});

		CumulativeProbabilityFromClassification fromClassification = new CumulativeProbabilityFromClassification(tagToIndex);
		int actual = 4;
		double[] apriori = {.1, .4};
		fromClassification.addPrediction(actual, apriori);
		double[] cumulativeProbability = fromClassification.buildCumulativeProbabilities();
		assertEquals(2, cumulativeProbability.length);
		assertEquals(0., cumulativeProbability[0], 0.0);
		assertEquals(1., cumulativeProbability[1], 0.0);
	}

	@Test
	public void oneScoreThreeClassesLastCorrect() {
		TagToIndex tagToIndex = new TagToIndex(new int[]{67, 4, 8});

		CumulativeProbabilityFromClassification fromClassification = new CumulativeProbabilityFromClassification(tagToIndex);
		int actual = 4;
		double[] apriori = {.1, .4, .5};
		fromClassification.addPrediction(actual, apriori);
		double[] cumulativeProbability = fromClassification.buildCumulativeProbabilities();
		assertEquals(3, cumulativeProbability.length);
		assertEquals(0., cumulativeProbability[0], 0.0);
		assertEquals(0., cumulativeProbability[1], 0.0);
		assertEquals(1., cumulativeProbability[2], 0.0);
	}

	@Test
	public void oneScoreThreeClassesMiddleCorrect() {
		TagToIndex tagToIndex = new TagToIndex(new int[]{67, 4, 8});

		CumulativeProbabilityFromClassification fromClassification = new CumulativeProbabilityFromClassification(tagToIndex);
		int actual = 8;
		double[] apriori = {.5, .3, .2};
		fromClassification.addPrediction(actual, apriori);
		double[] cumulativeProbability = fromClassification.buildCumulativeProbabilities();
		assertEquals(3, cumulativeProbability.length);
		assertEquals(0., cumulativeProbability[0], 0.0);
		assertEquals(1., cumulativeProbability[1], 0.0);
		assertEquals(1., cumulativeProbability[2], 0.0);
	}

	@Test
	public void threeScoresThreeClasses() {
		TagToIndex tagToIndex = new TagToIndex(new int[]{67, 4, 8});

		CumulativeProbabilityFromClassification fromClassification = new CumulativeProbabilityFromClassification(tagToIndex);
		fromClassification.addPrediction(4, new double[]{.5, .4, .1});
		fromClassification.addPrediction(67, new double[]{.1, .5, .3});
		fromClassification.addPrediction(4, new double[]{.5, .4, .1});

		double[] cumulativeProbability = fromClassification.buildCumulativeProbabilities();
		assertEquals(3, cumulativeProbability.length);
		assertEquals(2. / 3., cumulativeProbability[0], Double.MIN_VALUE);
		assertEquals(1.0, cumulativeProbability[1], Double.MIN_VALUE);
		assertEquals(1., cumulativeProbability[2], Double.MIN_VALUE);
	}

	@Test
	public void fromFailedTest() {
		double[] posteriori = Arrays.stream("5.1402268562570345E-155,1.2829610866964561E-17,1.268112967503341E-14,2.9293556168029276E-21,0.9999999999997364,3.4236732443866708E-18,8.557618954561047E-103,2.4434380613065004E-13,1.9629612205186163E-27,6.567312431206521E-15"
				.split(","))
				.mapToDouble(Double::parseDouble)
				.toArray();

		TagToIndex tagToIndex = new TagToIndex(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
		CumulativeProbabilityFromClassification fromClassification = new CumulativeProbabilityFromClassification(tagToIndex);
		fromClassification.addPrediction(4, posteriori);
		double[] probabilities = fromClassification.buildCumulativeProbabilities();
		assertEquals(1., probabilities[0], Double.MIN_VALUE);
	}
}