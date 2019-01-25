package com.mangione.continuous.stats;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.dense.DiscreteExemplarFactory;

public class ValueCountsTest {

	public static final Integer[][] DATA = new Integer[][]{{1, 3}, {3, 3}, {2, 2}};

	@Test
	public void valueCountsSortedByValue() {

		ArrayObservationProvider<Integer, DiscreteExemplar<Integer>> provider =
				new ArrayObservationProvider<>(DATA, new DiscreteExemplarFactory<>());

		ValueCounts valueCounts = new ValueCounts(provider, 0);
		List<Pair<Integer, Integer>> counts = valueCounts.getCounts();
		assertEquals(3, counts.size());
		testFirstAndSecond(counts.get(0), 1, 1);
		testFirstAndSecond(counts.get(1), 2, 1);
		testFirstAndSecond(counts.get(2), 3, 1);

		valueCounts = new ValueCounts(provider, 1);
		counts = valueCounts.getCounts();
		assertEquals(2, counts.size());
		testFirstAndSecond(counts.get(0), 2, 1);
		testFirstAndSecond(counts.get(1), 3, 2);
	}

	private void testFirstAndSecond(Pair<Integer, Integer> pair, Integer first, Integer second) {
		assertEquals(first, pair.getFirst());
		assertEquals(second, pair.getSecond());
	}

}