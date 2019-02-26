package com.mangione.continuous.calculators.chisquare;

import static com.mangione.continuous.calculators.chisquare.ProviderToChiSquareForFeatureTest.COUNTS;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ProxyValues;

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

		ProviderToChiSquareForAllFeatures providerToChiSquare = new ProviderToChiSquareForAllFeatures(provider, observationStates, targetStates);
		List<ChiSquare> chiSquares = providerToChiSquare.getChiSquares();
		assertEquals(2, chiSquares.size());
		assertEquals(1.51, chiSquares.get(0).getChiSquare(), 0.01);
		assertEquals(1.51, chiSquares.get(1).getChiSquare(), 0.01);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidObservationState()  {

	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidTargetState()  {

	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyProviderExcepts()  {

	}

	private ProxyValues fillProxies(String... names) {
		ProxyValues proxyValues = new ProxyValues();
		Arrays.stream(names)
				.forEach(proxyValues::add);
		return proxyValues;
	}
	
}