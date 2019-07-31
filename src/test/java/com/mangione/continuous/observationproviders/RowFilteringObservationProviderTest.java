package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.dense.Observation;

public class RowFilteringObservationProviderTest {
	private final static Integer[][] DATA = {{2, 3}, {4, 6}, {2, 0}};
	private ListObservationProvider<Integer, Observation<Integer>> provider;

	@Before
	public void setUp() {
		provider = new ArrayObservationProvider<>(DATA, (features) -> new Observation<>(Arrays.asList(features)));
	}

	@Test
	public void noRowFiltered() {
		RowFilteringObservationProvider<Integer, Observation<Integer>> rfop =
				new RowFilteringObservationProvider<>(provider, observation -> false);

		Iterator<Observation<Integer>> iterator = rfop.iterator();

		for (Integer[] row : DATA) {
			assertTrue(iterator.hasNext());
			assertArrayEquals(row, iterator.next().getFeatures().toArray());
		}
		assertFalse(iterator.hasNext());
	}

	@Test
	public void middleRowFiltered() {
		RowFilteringObservationProvider<Integer, Observation<Integer>> rfop =
				new RowFilteringObservationProvider<>(provider,
						observation -> observation.getFeatures().get(1).equals(6));

		Iterator<Observation<Integer>> iterator = rfop.iterator();

		assertEquals(2, rfop.getNumberOfLines());
		assertTrue(iterator.hasNext());

		assertArrayEquals(DATA[0], iterator.next().getFeatures().toArray());
		assertTrue(iterator.hasNext());
		assertArrayEquals(DATA[2], iterator.next().getFeatures().toArray());
		assertFalse(iterator.hasNext());
	}


	@Test
	public void lastRowFiltered() {
		RowFilteringObservationProvider<Integer, Observation<Integer>> rfop =
				new RowFilteringObservationProvider<>(provider,
						observation -> observation.getFeatures().get(1).equals(0));

		Iterator<Observation<Integer>> iterator = rfop.iterator();

		assertTrue(iterator.hasNext());
		assertArrayEquals(DATA[0], iterator.next().getFeatures().toArray());
		assertTrue(iterator.hasNext());
		assertArrayEquals(DATA[1], iterator.next().getFeatures().toArray());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void allRowsFiltered() {
		RowFilteringObservationProvider<Integer, Observation<Integer>> rfop =
				new RowFilteringObservationProvider<>(provider,
						observation -> true);
		assertEquals(0, rfop.getNumberOfLines());
		Iterator<Observation<Integer>> iterator = rfop.iterator();
		assertFalse(iterator.hasNext());
	}

	@Test
	public void numberOfLinesWhenEmpty() {
		ListObservationProvider<Integer, Observation<Integer>> empty =
				new ListObservationProvider<>(new ArrayList<>());

		RowFilteringObservationProvider<Integer, Observation<Integer>> rfop =
				new RowFilteringObservationProvider<>(empty,
						observation -> true);
		assertEquals(0, rfop.getNumberOfLines());
	}


	@Test
	public void callingHasNextMultipleTimesDoesNotAdvance() {
		RowFilteringObservationProvider<Integer, Observation<Integer>> rfop =
				new RowFilteringObservationProvider<>(provider,
						observation -> observation.getFeatures().get(1).equals(0));

		Iterator<Observation<Integer>> iterator = rfop.iterator();
		assertTrue(iterator.hasNext());
		assertArrayEquals(DATA[0], iterator.next().getFeatures().toArray());
		assertTrue(iterator.hasNext());
		assertArrayEquals(DATA[1], iterator.next().getFeatures().toArray());
		assertFalse(iterator.hasNext());
	}


	@Test(expected = IllegalArgumentException.class)
	public void nullConditionExcepts()  {
		new RowFilteringObservationProvider<>(provider, null);
	}

	@Test(expected = IllegalStateException.class)
	public void nextWhenNoneLeftExcepts() {
		ListObservationProvider<Integer, Observation<Integer>> empty =
				new ListObservationProvider<>(new ArrayList<>());

		RowFilteringObservationProvider<Integer, Observation<Integer>> rfop =
				new RowFilteringObservationProvider<>(empty,
						observation -> true);
		rfop.iterator().next();
	}
}