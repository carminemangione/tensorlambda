package com.mangione.continuous.calculators.stats;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

public class GroupedStatsFromProviderTest {
	@Test
	public void oneCategoryFirstColumn() {
		Object[][]  values = {{"one", 9.0}, {"one", 3.0}, {"one", 3.0}};
		ObservationProviderInterface<Object, ObservationInterface<Object>> provider = new ArrayObservationProvider<>(values,
				row->new Observation<>(Arrays.asList(row)));
		GroupedStatsFromProvider groupedStatsFromProvider = new GroupedStatsFromProvider(provider, observation -> observation.getFeature(0));
		Map<Object, List<ColumnStats>> statsMap = groupedStatsFromProvider.getStats();
		assertEquals(1, statsMap.size());
		List<ColumnStats> stats = statsMap.get("one");
		assertEquals(1, stats.size());
		assertEquals(5, stats.get(0).avg(), Double.MIN_VALUE);
	}

	@Test
	public void oneCategorySecondColumn() {
		Object[][]  values = {{9.0, "one"}, {3.0, "one"}, {3.0, "one"}};
		ObservationProviderInterface<Object, ObservationInterface<Object>> provider = new ArrayObservationProvider<>(values,
				row->new Observation<>(Arrays.asList(row)));
		GroupedStatsFromProvider groupedStatsFromProvider = new GroupedStatsFromProvider(provider, observation -> observation.getFeature(1));
		Map<Object, List<ColumnStats>> statsMap = groupedStatsFromProvider.getStats();
		assertEquals(1, statsMap.size());
		List<ColumnStats> stats = statsMap.get("one");
		assertEquals(5, stats.get(0).avg(), Double.MIN_VALUE);
	}

	@Test
	public void twoCategories() {
		Object[][]  values = {{"one", 9.0}, {"one", 3.0}, {"one", 3.0}, {"two", 1.0}, {"two", 3.0}, {"two", 3.0}};
		ObservationProviderInterface<Object, ObservationInterface<Object>> provider = new ArrayObservationProvider<>(values,
				row->new Observation<>(Arrays.asList(row)));
		GroupedStatsFromProvider groupedStatsFromProvider = new GroupedStatsFromProvider(provider, observation -> observation.getFeature(0));
		Map<Object, List<ColumnStats>> statsMap = groupedStatsFromProvider.getStats();
		assertEquals(2, statsMap.size());
		List<ColumnStats> stats = statsMap.get("one");
		assertEquals(5, stats.get(0).avg(), Double.MIN_VALUE);
		assertEquals(3, stats.get(0).getNumberOfValues());
		stats = statsMap.get("two");
		assertEquals(7./3., stats.get(0).avg(), Double.MIN_VALUE);
	}

	@Test
	public void nonNumericColumnsFiltered() {
		Object[][]  values = {{9.0, "one"}, {3.0, "one"}, {3.0, "one"}};
		ObservationProviderInterface<Object, ObservationInterface<Object>> provider = new ArrayObservationProvider<>(values,
				row->new Observation<>(Arrays.asList(row)));
		GroupedStatsFromProvider stats = new GroupedStatsFromProvider(provider, observation -> observation.getFeature(1));
		assertEquals(5., stats.getStats().get("one").get(0).avg(), 0);
	}
}





































































































































































































































































