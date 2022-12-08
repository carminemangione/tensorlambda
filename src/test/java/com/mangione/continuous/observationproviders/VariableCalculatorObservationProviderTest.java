package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.*;
import java.util.function.Function;

import org.junit.Before;

import com.mangione.continuous.calculators.StringToDoubleVariableCalculator;
import com.mangione.continuous.calculators.VariableCalculations;
import com.mangione.continuous.calculators.VariableCalculatorObservationProvider;
import com.mangione.continuous.observations.dense.Observation;
import com.mangione.continuous.observations.ObservationInterface;
import org.junit.Test;

public class VariableCalculatorObservationProviderTest {

	private final Double[][] converted = new Double[][]{{1d, 0d, 0d, 234d}, {0d, 1d, 0d, 321d}, {0d, 0d, 1d, 987d}};
	private VariableCalculatorObservationProvider<String, Double, ObservationInterface<Double>> variableCalculatorProvider;

	@Before
	public void setUp() {
		ArrayObservationProvider<String, ? extends ObservationInterface<String>> abcObservationProvider
				= new ArrayObservationProvider<String, ObservationInterface<String>>(new String[][]{{"a", "234"}, {"b", "321"}, {"c", "987"}},
               Observation::new);

		Function<String, List<Double>> stringListFunction = (feature) -> {
			Double[] out = new Double[]{0d, 0d, 0d};
			switch (feature) {
				case "a" -> out[0] = 1d;
				case "b" -> out[1] = 1d;
				case "c" -> out[2] = 1d;
			}
			return Arrays.asList(out);
		};

		VariableCalculations.Builder<String, Double> builder =
				new VariableCalculations.Builder<>();
		builder
				.addListCalculator(0, stringListFunction)
		.setDefaultListCalculator(new StringToDoubleVariableCalculator());

		variableCalculatorProvider = new VariableCalculatorObservationProvider<>(abcObservationProvider,
				builder.build(),
				list->new Observation<>(list.toArray(new Double[0])), String[]::new);
	}

	@Test
	public void variableCalculatorWithDefault() {
		int i = 0;
		for (ObservationInterface<Double> anOp : variableCalculatorProvider) {
			assertArrayEquals(converted[i++], anOp.getFeatures(Double[]::new));
		}
		assertEquals(3, i);
	}

	@Test
	public void forEach() {
		final int[] i = {0};
		variableCalculatorProvider.forEach(observation -> assertArrayEquals(converted[i[0]++], observation.getFeatures(Double[]::new)));
		assertEquals(3, i[0]);
	}

	@Test
	public void forEachRemaining() {
		final int[] i = {1};
		Iterator<ObservationInterface<Double>> iterator = variableCalculatorProvider.iterator();
		iterator.next();
		iterator.forEachRemaining(observation -> assertArrayEquals(converted[i[0]++], observation.getFeatures(Double[]::new)));
		assertEquals(3, i[0]);
	}

	@Test
	public void remove() {
		Iterator<ObservationInterface<Double>> iterator = variableCalculatorProvider.iterator();
		iterator.next();
		iterator.remove();
		assertArrayEquals(converted[2], iterator.next().getFeatures(Double[]::new));

		iterator = variableCalculatorProvider.iterator();
		assertArrayEquals(converted[0], iterator.next().getFeatures(Double[]::new));
		assertArrayEquals(converted[2], iterator.next().getFeatures(Double[]::new));
		assertFalse(iterator.hasNext());

	}

	@Test
	public void multipleIterators() {
		final int[] i = {0};
		variableCalculatorProvider.forEach(observation -> {
			assertArrayEquals(converted[i[0]++], observation.getFeatures(Double[]::new));
			final int[] inner = {0};
			variableCalculatorProvider.forEach(
					innerObs -> assertArrayEquals(converted[inner[0]++], innerObs.getFeatures(Double[]::new)));
		});

		assertEquals(3, i[0]);
	}
}
