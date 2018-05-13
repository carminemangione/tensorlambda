package com.mangione.continuous.classifiers;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.observations.ExemplarInterface;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class NaiveBayesClassifierTest {

	@Test
	public void testClassification() throws Exception {
		List<ExemplarInterface<Double, Integer>> exemplars = new ArrayList<>();
		// lasagna, die, dog, ate, friend, best, for
		exemplars.add(new DiscreteExemplar<>(Arrays.asList(2., 1., 0., 0., 0., 0., 1.), 1., 1));
		exemplars.add(new DiscreteExemplar<>(Arrays.asList(1., 0., 2., 1., 0., 0., 0.), 0., 0));
		exemplars.add(new DiscreteExemplar<>(Arrays.asList(0., 0., 2., 0., 2., 1., 0.), 1., 1));
		exemplars.add(new DiscreteExemplar<>(Arrays.asList(1., 0., 0., 0., 2., 2., 0.), 1., 1));

		ObservationProvider<Double, ExemplarInterface<Double, Integer>> provider =
				new ArrayObservationProvider<>(exemplars, new DiscreteExemplarFactory());

		NaiveBayesClassifier nbc = new NaiveBayesClassifier(provider, 2);
		assertEquals(1, nbc.classify(new DiscreteExemplar<>(Arrays.asList(0., 1., 0., 0., 2., 1., 0.), 1., 1)));
	}

	@Test
	public void planeBoundary() throws Exception {
		List<DiscreteExemplar<Double>> exemplars = new ArrayList<>();

		exemplars.add(new DiscreteExemplar<>(Arrays.asList(20., 1.), 1., 1));
		exemplars.add(new DiscreteExemplar<>(Arrays.asList(1., 4.), 0., 0));
		exemplars.add(new DiscreteExemplar<>(Arrays.asList(20., 2.), 1., 1));
		exemplars.add(new DiscreteExemplar<>(Arrays.asList(1., 5.), 0., 0));

		ObservationProvider<Double, ExemplarInterface<Double, Integer>> provider =
				new ArrayObservationProvider<>(exemplars, new DiscreteExemplarFactory());
		NaiveBayesClassifier nbc = new NaiveBayesClassifier(provider, 2);
		assertEquals(1, nbc.classify(new DiscreteExemplar<>(Arrays.asList(20., 5.), 1., 1)));
		assertEquals(0, nbc.classify(new DiscreteExemplar<>(Arrays.asList(1., 15.), 0., 0)));

	}
}