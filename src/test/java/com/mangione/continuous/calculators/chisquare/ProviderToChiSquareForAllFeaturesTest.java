package com.mangione.continuous.calculators.chisquare;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProviderToChiSquareForAllFeaturesTest {
	
	@Test
	// Taken from http://www.stat.yale.edu/Courses/1997-98/101/chisq.htm
	public void chiSquareTwoColumnsOnlyDoesOnePass() {
		ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> provider =
				new ProviderToChiSquareForFeatureTest.ContingencyTableExemplarProvider(null, 2) {
					private int numberOfTimesIteratorWasCalled;

					@Nonnull
					@Override
					public Iterator<ExemplarInterface<Integer, Integer>> iterator() {
						if (numberOfTimesIteratorWasCalled++ > 1)
							throw new IllegalStateException("iterator should only be called " +
									"once for feature count and once for to process all columns...");
						return super.iterator();
					}
				};

		ProxyValues observationStates = fillProxies("obs1", "obs2", "obs3");
		ProxyValues targetStates = fillProxies("target1", "target2", "target3");

		ProviderToChiSquareForAllFeatures providerToChiSquare = new ProviderToChiSquareForAllFeatures(provider,
				observationStates, targetStates, 2);
		List<ChiSquare> chiSquares = providerToChiSquare.getChiSquares();
		assertEquals(2, chiSquares.size());
		assertEquals(1.51, chiSquares.get(0).getChiSquare(), 0.01);
		assertEquals(1.51, chiSquares.get(1).getChiSquare(), 0.01);
	}

	@Test
	// Taken from http://www.stat.yale.edu/Courses/1997-98/101/chisq.htm
	public void batchSizeOfOneGoesInTwoPasses() {
		final int[] numTimesThrough = {0};
		ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> provider =
				new ProviderToChiSquareForFeatureTest.ContingencyTableExemplarProvider(null, 2) {
					@Nonnull
					@Override
					public Iterator<ExemplarInterface<Integer, Integer>> iterator() {
						if (numTimesThrough[0]++ > 2)
							throw new IllegalStateException("iterator should only be called " +
									"once for feature count and once for to process all columns...");
						return super.iterator();
					}
				};

		ProxyValues observationStates = fillProxies("obs1", "obs2", "obs3");
		ProxyValues targetStates = fillProxies("target1", "target2", "target3");

		ProviderToChiSquareForAllFeatures providerToChiSquare = new ProviderToChiSquareForAllFeatures(provider, observationStates, targetStates, 1);
		List<ChiSquare> chiSquares = providerToChiSquare.getChiSquares();
		assertEquals(3, numTimesThrough[0]);
		assertEquals(2, chiSquares.size());
		assertEquals(1.51, chiSquares.get(0).getChiSquare(), 0.01);
		assertEquals(1.51, chiSquares.get(1).getChiSquare(), 0.01);
	}

	@Test
	public void missingValuesInSparseArrayAreStillCounted() {
		int[][] counts = {{4, 1}, {2, 3}};
		ContingencyTable table = ContingencyTableTest.createContingencyTableFromCounts(counts);
		ChiSquare baseChiSquare = new ChiSquare(table);

		List<SparseExemplar<Integer>> exemplars = Arrays.asList(
				getSparseExemplarWith(2, 0, 0),
				getSparseExemplarWith(2, 0, 0),
				getSparseExemplarWith(2, 0, 0),
				getSparseExemplarWith(2, 0, 0),
				getSparseExemplarWith(2, 0, 1),
				getSparseExemplarWith(2, 1, 0),
				getSparseExemplarWith(2, 1, 0),
				getSparseExemplarWith(2, 1, 1),
				getSparseExemplarWith(2, 1, 1),
				getSparseExemplarWith(2, 1, 1));

		ProxyValues observationStates = fillProxies("obs1", "obs2");
		ProxyValues targetStates = fillProxies("target1", "target2");
		ArrayObservationProvider<Integer, SparseExemplar<Integer>> sparseExemplars = new ArrayObservationProvider<>(exemplars, null);
		ProviderToChiSquareForAllFeatures providerToChiSquare = new ProviderToChiSquareForAllFeatures(sparseExemplars, observationStates, targetStates, 1);
		assertEquals(baseChiSquare.getChiSquare(), providerToChiSquare.getChiSquares().get(0).getChiSquare(), 0.001);

	}


	@Test(expected = IllegalArgumentException.class)
	public void invalidObservationState()  {
		ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> provider =
				new ProviderToChiSquareForFeatureTest.ContingencyTableExemplarProvider(null, 2);

		ProxyValues observationStatesOneTooLittle = fillProxies("obs1", "obs2");
		ProxyValues targetStates = fillProxies("target1", "target2", "target3");
		new ProviderToChiSquareForAllFeatures(provider, observationStatesOneTooLittle, targetStates, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidTargetState()  {
		ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> provider =
				new ProviderToChiSquareForFeatureTest.ContingencyTableExemplarProvider(null, 2);

		ProxyValues observationStates = fillProxies("obs1", "obs2", "obs3");
		ProxyValues targetStatesMissingOne = fillProxies("target1", "target2");
		new ProviderToChiSquareForAllFeatures(provider, observationStates, targetStatesMissingOne, 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyProviderExcepts()  {
		new ProviderToChiSquareForAllFeatures(new EmptyProvider(null), new ProxyValues(), new ProxyValues(), 2);
	}

	private ProxyValues fillProxies(String... names) {
		ProxyValues proxyValues = new ProxyValues();
		Arrays.stream(names)
				.forEach(proxyValues::add);
		return proxyValues;
	}



	private SparseExemplar<Integer> getSparseExemplarWith(int numColumns, int... values) {
		List<Integer> nonZeroValues = new ArrayList<>();
		List<Integer> nonZeroIndexes = new ArrayList<>();

		for (int i = 0; i < values.length; i++) {
			if (values[i] > 0 || i == values.length - 1) {
				nonZeroIndexes.add(i);
				nonZeroValues.add(values[i]);
			}
		}


		return new SparseExemplar<>(nonZeroValues.toArray(new Integer[0]),
				nonZeroIndexes.stream().mapToInt(Integer::intValue).toArray(),
				numColumns, 0, numColumns - 1);
	}


	private static class EmptyProvider extends ObservationProvider<Integer, ExemplarInterface<Integer, Integer>> {
		EmptyProvider(ObservationFactoryInterface<Integer, ? extends ExemplarInterface<Integer, Integer>> factory) {
			super(factory);
		}

		@Nonnull
		@Override
		public Iterator<ExemplarInterface<Integer, Integer>> iterator() {
			return new Iterator<ExemplarInterface<Integer, Integer>>() {
				@Override
				public boolean hasNext() {
					return false;
				}

				@Override
				public ExemplarInterface<Integer, Integer> next() {
					return null;
				}
			};
		}
	}
}