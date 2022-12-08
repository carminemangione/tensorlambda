package com.mangione.continuous.classifiers.supervised.ranking.xgboost;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.dense.DiscreteExemplar;

public class GroupsTest {
	@Test
	public void oneGroup() {
		Integer[][]  values = {{1, 2}, {2, 2}, {3, 2}};
		ArrayObservationProvider<Integer, DiscreteExemplar> provider = new ArrayObservationProvider<>(values,
				ints -> new DiscreteExemplar(Arrays.asList(ints), 1));
		Groups<Integer, DiscreteExemplar> groups = new Groups<>(provider, ex -> ex.getFeature(1));
		int[] groupCounts = groups.getGroups();
		assertEquals(1, groupCounts.length);
		assertEquals(3, groupCounts[0]);
	}

	@Test
	public void twoGroups() {
		Integer[][]  values = {{1, 2}, {2, 2}, {3, 2}, {2, 1}, {3, 1}};
		ArrayObservationProvider<Integer, DiscreteExemplar> provider = new ArrayObservationProvider<>(values,
				ints -> new DiscreteExemplar(Arrays.asList(ints), 1));
		Groups<Integer, DiscreteExemplar> groups = new Groups<>(provider, ex -> ex.getFeature(1));
		int[] groupCounts = groups.getGroups();
		assertEquals(2, groupCounts.length);
		assertEquals(3, groupCounts[0]);
		assertEquals(2, groupCounts[1]);
	}

	@Test
	public void threeGroupsLastOneIsNewGroup() {
		Integer[][]  values = {{1, 2}, {2, 2}, {3, 2}, {2, 1}, {3, 1}, {3, 0}};
		ArrayObservationProvider<Integer, DiscreteExemplar> provider = new ArrayObservationProvider<>(values,
				ints -> new DiscreteExemplar(Arrays.asList(ints), 1));
		Groups<Integer, DiscreteExemplar> groups = new Groups<>(provider, ex -> ex.getFeature(1));
		int[] groupCounts = groups.getGroups();
		assertEquals(3, groupCounts.length);
		assertEquals(3, groupCounts[0]);
		assertEquals(2, groupCounts[1]);
		assertEquals(1, groupCounts[2]);
	}
}