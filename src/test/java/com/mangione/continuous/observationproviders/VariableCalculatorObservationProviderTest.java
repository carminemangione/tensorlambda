package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.*;

import org.junit.Before;

import com.mangione.continuous.calculators.StringToDoubleVariableCalculator;
import com.mangione.continuous.calculators.VariableCalculations;
import com.mangione.continuous.calculators.VariableCalculator;
import com.mangione.continuous.calculators.VariableCalculatorObservationProvider;
import com.mangione.continuous.observations.dense.Observation;
import com.mangione.continuous.observations.ObservationInterface;
import org.junit.Test;

public class VariableCalculatorObservationProviderTest {

	private Double[][] CONVERTED = new Double[][]{{1d, 0d, 0d, 234d}, {0d, 1d, 0d, 321d}, {0d, 0d, 1d, 987d}};
	private VariableCalculatorObservationProvider<String, Double, ObservationInterface<Double>> variableCalculatorProvider;

	@Before
	public void setUp() {
		Map<Integer, VariableCalculator<String, Double>> calculators = new HashMap<>();

		ArrayObservationProvider<String, ? extends ObservationInterface<String>> abcObservationProvider
				= new ArrayObservationProvider<String, ObservationInterface<String>>(new String[][]{{"a", "234"}, {"b", "321"}, {"c", "987"}},
                (features1) -> new Observation<>(Arrays.asList(features1)));

		calculators.put(0, (feature, features) -> {
			Double[] out = new Double[]{0d, 0d, 0d};
			switch (feature) {
				case "a":
					out[0] = 1d;
					break;
				case "b":
					out[1] = 1d;
					break;
				case "c":
					out[2] = 1d;
					break;
			}
			return Arrays.asList(out);
		});
		variableCalculatorProvider = new VariableCalculatorObservationProvider<>(abcObservationProvider,
				new VariableCalculations<>(calculators, new StringToDoubleVariableCalculator()),
				Observation::new);
	}

	@Test
	public void variableCalculatorWithDefault() {
		int i = 0;
		for (ObservationInterface<Double> anOp : variableCalculatorProvider) {
			assertArrayEquals(CONVERTED[i++], anOp.getFeatures().toArray());
		}
		assertEquals(3, i);
	}

	@Test
	public void forEach() {
		final int[] i = {0};
		variableCalculatorProvider.forEach(observation -> assertArrayEquals(CONVERTED[i[0]++], observation.getFeatures().toArray()));
		assertEquals(3, i[0]);
	}

	@Test
	public void forEachRemaining() {
		final int[] i = {1};
		Iterator<ObservationInterface<Double>> iterator = variableCalculatorProvider.iterator();
		iterator.next();
		iterator.forEachRemaining(observation -> assertArrayEquals(CONVERTED[i[0]++], observation.getFeatures().toArray()));
		assertEquals(3, i[0]);
	}

	@Test
	public void remove() {
		Iterator<ObservationInterface<Double>> iterator = variableCalculatorProvider.iterator();
		iterator.next();
		iterator.remove();
		assertArrayEquals(CONVERTED[2], iterator.next().getFeatures().toArray());

		iterator = variableCalculatorProvider.iterator();
		assertArrayEquals(CONVERTED[0], iterator.next().getFeatures().toArray());
		assertArrayEquals(CONVERTED[2], iterator.next().getFeatures().toArray());
		assertFalse(iterator.hasNext());

	}

	@Test
	public void multipleIterators() {
		final int[] i = {0};
		variableCalculatorProvider.forEach(observation -> {
			assertArrayEquals(CONVERTED[i[0]++], observation.getFeatures().toArray());
			final int[] inner = {0};
			variableCalculatorProvider.forEach(
					innerObs -> assertArrayEquals(CONVERTED[inner[0]++], innerObs.getFeatures().toArray()));
		});

		assertEquals(3, i[0]);
	}
}
