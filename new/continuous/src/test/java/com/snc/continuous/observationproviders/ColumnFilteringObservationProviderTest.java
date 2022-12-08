package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.dense.Observation;

public class ColumnFilteringObservationProviderTest {

	private Function<Integer[], Observation<Integer>> observationFunction;

	@Before
	public void setUp() {
		observationFunction = Observation::new;
	}

	@Test
	public void provideOneFilteredColumn() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};

		ObservationProviderInterface<Integer, Observation<Integer>> aop =
				new ArrayObservationProvider<>(observations, observationFunction);

		ColumnFilteringObservationProvider<Integer, Observation<Integer>> cfop = new ColumnFilteringObservationProvider<>(aop,
				new HashSet<>(Arrays.asList(0, 2)), (values, columns)->new Observation<>(values), Integer[]::new);

	    Iterator<Observation<Integer>> iterator = cfop.iterator();
		assertTrue(iterator.hasNext());
		Observation<Integer> next = iterator.next();
		assertArrayEquals(new Integer[]{2}, next.getFeatures(Integer[]::new));

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertArrayEquals(new Integer[]{5}, next.getFeatures(Integer[]::new));
		assertFalse(iterator.hasNext());
	}

	@Test
	public void doesNotCallGetFeaturesBecauseItExpandsSparseArrays() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};
		ExceptionIfGetFeaturesCalledObservationProvider provider
				= new ExceptionIfGetFeaturesCalledObservationProvider(observations, observationFunction);

		ColumnFilteringObservationProvider<Integer, Observation<Integer>> cfop =
				new ColumnFilteringObservationProvider<>(provider,
						new HashSet<>(Arrays.asList(0, 2)), (values, columns) -> new Observation<>(values), Integer[]::new);

		Iterator<Observation<Integer>> iterator = cfop.iterator();
		//noinspection WhileLoopReplaceableByForEach
		while(iterator.hasNext())
			iterator.next();
	}

	@Test
	public void coerceNonIntegerTypes() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};

		ObservationProviderInterface<Integer, Observation<Integer>> aop =
				new ArrayObservationProvider<>(observations, observationFunction);

		ColumnFilteringObservationProvider<Integer, Observation<Integer>> cfop = new ColumnFilteringObservationProvider<>(aop,
				new HashSet<>(Arrays.asList(0L, 2L)), (values, columns)->new Observation<>(values), Integer[]::new);

		Iterator<Observation<Integer>> iterator = cfop.iterator();
		assertTrue(iterator.hasNext());
		Observation<Integer> next = iterator.next();
		assertArrayEquals(new Integer[]{2}, next.getFeatures(Integer[]::new));

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertArrayEquals(new Integer[]{5}, next.getFeatures(Integer[]::new));
		assertFalse(iterator.hasNext());
	}

	private static class ExceptionIfGetFeaturesCalledObservationProvider
			extends ArrayObservationProvider<Integer, Observation<Integer>> {

		private ExceptionIfGetFeaturesCalledObservationProvider(Integer[][] data, Function<Integer[],
				Observation<Integer>> observationCoercionInterface) {
			super(data, observationCoercionInterface);
		}

	}

}