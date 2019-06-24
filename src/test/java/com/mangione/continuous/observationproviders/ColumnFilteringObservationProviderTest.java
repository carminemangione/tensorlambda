package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.dense.Observation;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

import static org.junit.Assert.*;

public class ColumnFilteringObservationProviderTest {

	private Function<Integer[], Observation<Integer>> observationFunction;

	@Before
	public void setUp() {
		observationFunction = integers -> new Observation<>(Arrays.asList(integers));
	}

	@Test
	public void provideOneFilteredColumn() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};

		ObservationProviderInterface<Integer, Observation<Integer>> aop =
				new ArrayObservationProvider<>(observations, observationFunction);

		ColumnFilteringObservationProvider<Integer, Observation<Integer>> cfop = new ColumnFilteringObservationProvider<>(aop,
				new int[]{0, 2}, (values, columns)->new Observation<>(values));

	    Iterator<Observation<Integer>> iterator = cfop.iterator();
		assertTrue(iterator.hasNext());
		Observation<Integer> next = iterator.next();
		assertEquals(Collections.singletonList(2), next.getFeatures());

		assertTrue(iterator.hasNext());
		next = iterator.next();
		assertEquals(Collections.singletonList(5), next.getFeatures());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void doesNotCallGetFeaturesBecauseItExpandsSparseArrays() {
		Integer[][] observations = {{1, 2, 3}, {4, 5, 6}};
		ExceptionIfGetFeaturesCalledObservationProvider provider
				= new ExceptionIfGetFeaturesCalledObservationProvider(observations, observationFunction);

		ColumnFilteringObservationProvider<Integer, Observation<Integer>> cfop =
				new ColumnFilteringObservationProvider<>(provider,
						new int[]{0, 2}, (values, columns) -> new Observation<>(values));

		Iterator<Observation<Integer>> iterator = cfop.iterator();
		//noinspection WhileLoopReplaceableByForEach
		while(iterator.hasNext())
			iterator.next();
	}


	private static class ExceptionIfGetFeaturesCalledObservationProvider
			extends ArrayObservationProvider<Integer, Observation<Integer>> {

		private ExceptionIfGetFeaturesCalledObservationProvider(Integer[][] data, Function<Integer[],
				Observation<Integer>> observationCoercionInterface) {
			super(data, observationCoercionInterface);
		}

	}

}