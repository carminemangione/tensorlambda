package com.mangione.continuous.sampling;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observationproviders.RandomGeneratorFactory;
import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class SampledObservationProviderTest {
	private static final Double[][] DATA = new Double[][]{{3d}, {5d}, {6d}, {7d}, {9d}};
	private ObservationProviderInterface<Double, ObservationInterface<Double>> observationProvider;
	private int seed;
	private SampledObservationProvider<Double, ObservationInterface<Double>> trainsSampler;
	private double[] trainExpected;
	private double[] testExpected;
	private boolean[] inTestSet;


	@Before
	public void setUp() throws Exception {
		observationProvider = new ArrayObservationProvider<>(DATA, new DoubleObservationFactory());
		seed = 1000;

		inTestSet = new boolean[]{true, false, false, true, false};
		trainsSampler = new SampledObservationProvider<>(0.50, observationProvider, new DoubleObservationFactory(),
				new AlternatingTestRandomGeneratorFactory(inTestSet), seed, false);


		trainExpected = new double[]{5d, 6d, 9d};
		testExpected = new double[]{3d, 7d};

	}

	@Test
	public void iteratorTest() throws Exception {
		SampledObservationProvider<Double, ObservationInterface<Double>> sampler =
				new SampledObservationProvider<>(0.50, observationProvider, new DoubleObservationFactory(),
						new AlternatingTestRandomGeneratorFactory(inTestSet), seed, true);

		int i = 0;
		for (ObservationInterface<Double> observation : sampler) {
			assertEquals(testExpected[i++], observation.getFeatures().get(0), 0);
		}
		assertEquals(2, i);
	}

	@Test
	public void iteratorTrain() throws Exception {
		int i = 0;
		for (ObservationInterface<Double> aSampler : trainsSampler) {
			assertEquals(trainExpected[i++], aSampler.getFeatures().get(0), 0);
		}
		assertEquals(2, i);
	}

	@Test
	public void forEach() throws Exception {
		final int[] i = {0};
		trainsSampler.forEach(observation -> assertEquals(trainExpected[i[0]++], observation.getFeatures().get(0), 0));
		assertEquals(2, i[0]);
	}

	@Test
	public void noSamplesDoesNotExplode() throws Exception {
		ObservationProviderInterface<Double, ObservationInterface<Double>> emptyProvider =
				new ArrayObservationProvider<>(new Double[0][0], new DoubleObservationFactory());
		trainsSampler = new SampledObservationProvider<>(0.50, emptyProvider, new DoubleObservationFactory(),
				new AlternatingTestRandomGeneratorFactory(inTestSet), seed, false);
		assertFalse(trainsSampler.iterator().hasNext());
	}


	@Test
	public void forEachRemaining() throws Exception {
		Iterator<ObservationInterface<Double>> iterator = trainsSampler.iterator();
		assertTrue(iterator.hasNext());
		assertEquals(5d, iterator.next().getFeatures().get(0), 0);
		final int[] i = {1};
		iterator.forEachRemaining(observation -> assertEquals(trainExpected[i[0]++], observation.getFeatures().get(0), 0));
	}


	@Test(expected = UnsupportedOperationException.class)
	public void spliterator() throws Exception {
		trainsSampler.spliterator();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void remove() throws Exception {
		trainsSampler.iterator().remove();
	}

	@Test
	public void multipleIterators() throws Exception {
		final int[] i = {0};
		trainsSampler.forEach(observation ->
		{
			assertEquals(trainExpected[i[0]++], observation.getFeatures().get(0), 0);
			final int[] inner = {0};
			trainsSampler.forEach(innerObs -> assertEquals(trainExpected[inner[0]++], innerObs.getFeatures().get(0), 0));
			assertEquals(2, inner[0]);
		});
		assertEquals(2, i[0]);
	}

	private class AlternatingTestRandomGeneratorFactory implements RandomGeneratorFactory {
		private final boolean[] inTestSet;

		private AlternatingTestRandomGeneratorFactory(boolean[] inTestSet) {
			this.inTestSet = inTestSet;
		}

		@Override
		public RandomGenerator generate(long seed) {
			assertEquals(SampledObservationProviderTest.this.seed, seed);

			return new RandomGenerator() {
				int currentCount = 0;

				@Override
				public void setSeed(int seed) {

				}

				@Override
				public void setSeed(int[] seed) {

				}

				@Override
				public void setSeed(long seed) {

				}

				@Override
				public void nextBytes(byte[] bytes) {

				}

				@Override
				public int nextInt() {
					return 0;
				}

				@Override
				public int nextInt(int n) {
					return 0;
				}

				@Override
				public long nextLong() {
					return 0;
				}

				@Override
				public boolean nextBoolean() {
					return false;
				}

				@Override
				public float nextFloat() {
					return 0;
				}

				@Override
				public double nextDouble() {
					return inTestSet[currentCount++] ? 0 : 1;
				}

				@Override
				public double nextGaussian() {
					return 0;
				}
			};
		}
	}

}

