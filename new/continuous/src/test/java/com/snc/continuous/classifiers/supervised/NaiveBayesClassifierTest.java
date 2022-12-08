package com.mangione.continuous.classifiers.supervised;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.dense.ContinuousExemplar;


public class NaiveBayesClassifierTest {

	@Test
	public void testClassification() {
		List<ContinuousExemplar> exemplars = new ArrayList<>();
		// lasagna, die, dog, ate, friend, best, for
		exemplars.add(new ContinuousExemplar(new double[] {2., 1., 0., 0., 0., 0., 1.}, 1.));
		exemplars.add(new ContinuousExemplar(new double[] {1., 0., 2., 1., 0., 0., 0.}, 0.));
		exemplars.add(new ContinuousExemplar(new double[] {0., 0., 2., 0., 2., 1., 0.}, 1.));
		exemplars.add(new ContinuousExemplar(new double[] {1., 0., 0., 0., 2., 2., 0.}, 1));

		ObservationProviderInterface<Double, ContinuousExemplar> provider =
				new ListObservationProvider<>(exemplars);

		NaiveBayesClassifier nbc = new NaiveBayesClassifier(provider, 2);
		assertEquals(1, nbc.classify(new ContinuousExemplar(new double[] {0., 1., 0., 0., 2., 1., 0.}, 1.)));
	}

	@Test
	public void planeBoundary() {
		List<ContinuousExemplar> exemplars = new ArrayList<>();

		exemplars.add(new ContinuousExemplar(new double[] {20., 1.}, 1.));
		exemplars.add(new ContinuousExemplar(new double[] {1., 4.}, 0.));
		exemplars.add(new ContinuousExemplar(new double[] {20., 2.}, 1.));
		exemplars.add(new ContinuousExemplar(new double[] {1., 5.}, 0.));

		ObservationProviderInterface<Double, ContinuousExemplar> provider =
				new ListObservationProvider<>(exemplars);
		NaiveBayesClassifier nbc = new NaiveBayesClassifier(provider, 2);
		assertEquals(1, nbc.classify(new ContinuousExemplar(new double[] {20., 5.}, 1.)));
		assertEquals(0, nbc.classify(new ContinuousExemplar(new double[] {1., 15.}, 0.)));

	}
}