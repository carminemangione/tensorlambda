package com.mangione.continuous.calculators.stats;

import static java.lang.Double.MIN_VALUE;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.dense.Observation;

public class StatsFromProviderTest {

	@Test
	public void testStats() {
		double[][] values = {{2, 3}, {1, 4}};
		ObservationProviderInterface<Double, ObservationInterface<Double>> provider = new ArrayObservationProvider<>(
				ArrayObservationProvider.doubleFromPrimitive(values), Observation::new);

		StatsFromProvider statsFromProvider = new StatsFromProvider(provider, 10);
		List<ColumnStats> columnStats = statsFromProvider.getColumnStats();
		assertEquals(2, columnStats.size());
		assertEquals(1.5, columnStats.get(0).avg(), MIN_VALUE);
		assertEquals(3.5, columnStats.get(1).avg(), MIN_VALUE);
		assertEquals(10, columnStats.get(0).getHistogram().length);
	}
}