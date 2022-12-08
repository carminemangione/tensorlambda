package com.mangione.continuous.calculators.stats;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AllColumnsStatsTest {
	@Test
	public void oneColumn() {
		int numberOfColumns = 1;
		AllColumnsStats allColumnsStats = new AllColumnsStats(numberOfColumns);
		List<Integer> features = Collections.singletonList(2);
		allColumnsStats.add(features);
		List<ColumnStats> columnStats = allColumnsStats.getColumnStats();
		assertEquals(1, columnStats.size());
		assertEquals(2, columnStats.get(0).avg(), Double.MIN_VALUE);
	}

	@Test
	public void twoColumns() {
		int numberOfColumns = 2;
		AllColumnsStats allColumnsStats = new AllColumnsStats(numberOfColumns);
		List<Integer> features = Arrays.asList(2, 4);
		allColumnsStats.add(features);
		List<ColumnStats> columnStats = allColumnsStats.getColumnStats();
		assertEquals(2, columnStats.size());
		assertEquals(2, columnStats.get(0).avg(), Double.MIN_VALUE);
		assertEquals(4, columnStats.get(1).avg(), Double.MIN_VALUE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooManyFeatures()  {
		new AllColumnsStats(2).add(Arrays.asList(2, 3, 4));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooFewFeatures()  {
		new AllColumnsStats(5).add(Arrays.asList(2, 3, 4));

	}
}