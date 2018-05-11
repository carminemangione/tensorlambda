package com.mangione.continuous.classifiers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mangione.cse151.observationproviders.ArrayObservationProvider;
import com.mangione.cse151.observationproviders.ObservationProvider;
import com.mangione.cse151.observations.DiscreteExemplar;

public class NaiveBayesClassifierTest {

	@Test
	public void testClassification() throws Exception {
		List<DiscreteExemplar> exemplars = new ArrayList<>();
		// lasagna, die, dog, ate, friend, best, for
		exemplars.add(new DiscreteExemplar(new double[]{2,1,0,0,0,0,1}, 1, 1));
		exemplars.add(new DiscreteExemplar(new double[]{1,0,2,1,0,0,0}, 0, 0));
		exemplars.add(new DiscreteExemplar(new double[]{0,0,2,0,2,1,0}, 1, 1));
		exemplars.add(new DiscreteExemplar(new double[]{1,0,0,0,2,2,0}, 1, 1));

		ObservationProvider<DiscreteExemplar> provider = new ArrayObservationProvider<>(exemplars, DiscreteExemplar::new);
		NaiveBayesClassifier nbc = new NaiveBayesClassifier(provider, 2);
		assertEquals(1, nbc.classify(new DiscreteExemplar(new double[]{0,1,0,0,2,1,0}, 1, 1)));
	}

	@Test
	public void planeBoundary() throws Exception {
		List<DiscreteExemplar> exemplars = new ArrayList<>();

		exemplars.add(new DiscreteExemplar(new double[]{20,1}, 1, 1));
		exemplars.add(new DiscreteExemplar(new double[]{1,4}, 0, 0));
		exemplars.add(new DiscreteExemplar(new double[]{20,2}, 1, 1));
		exemplars.add(new DiscreteExemplar(new double[]{1,5}, 0, 0));

		ObservationProvider<DiscreteExemplar> provider = new ArrayObservationProvider<>(exemplars, DiscreteExemplar::new);
		NaiveBayesClassifier nbc = new NaiveBayesClassifier(provider, 2);
		assertEquals(1, nbc.classify(new DiscreteExemplar(new double[]{20,5}, 1, 1)));
		assertEquals(0, nbc.classify(new DiscreteExemplar(new double[]{1,15}, 0, 0)));

	}



}