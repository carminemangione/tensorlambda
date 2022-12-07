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

	private Double[][] CONVERTED = new Double[][]{{1d, 0d, 0d, 234d}, {0d, 1d, 0d, 321d}, {0d, 0d, 1d, 987d}};
	private VariableCalculatorObservationProvider<String, Double, ObservationInterface<Double>> variableCalculatorProvider;

	@Before
	public void setUp() {
		ArrayObservationProvider<String, ? extends ObservationInterface<String>> abcObservationProvider
				= new ArrayObservationProvider<String, ObservationInterface<String>>(new String[][]{{"a", "234"}, {"b", "321"}, {"c", "987"}},
<<<<<<< HEAD
                (features1) -> new Observation<>(Arrays.asList(features1)));
=======
               Observation::new);
>>>>>>> 73d9563 (Migrated file changes from the source.)

		Function<String, List<Double>> stringListFunction = (feature) -> {
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
		};

		VariableCalculations.Builder<String, Double> builder =
				new VariableCalculations.Builder<>();
		builder
				.addListCalculator(0, stringListFunction)
		.setDefaultListCalculator(new StringToDoubleVariableCalculator());

		variableCalculatorProvider = new VariableCalculatorObservationProvider<>(abcObservationProvider,
				builder.build(),
<<<<<<< HEAD
				Observation::new);
=======
				list->new Observation<>(list.toArray(new Double[0])), String[]::new);
>>>>>>> 73d9563 (Migrated file changes from the source.)
	}

	@Test
	public void variableCalculatorWithDefault() {
		int i = 0;
		for (ObservationInterface<Double> anOp : variableCalculatorProvider) {
			assertArrayEquals(CONVERTED[i++], anOp.getFeatures(Double[]::new));
		}
		assertEquals(3, i);
	}

	@Test
	public void forEach() {
		final int[] i = {0};
		variableCalculatorProvider.forEach(observation -> assertArrayEquals(CONVERTED[i[0]++], observation.getFeatures(Double[]::new)));
		assertEquals(3, i[0]);
	}

	@Test
	public void forEachRemaining() {
		final int[] i = {1};
		Iterator<ObservationInterface<Double>> iterator = variableCalculatorProvider.iterator();
		iterator.next();
		iterator.forEachRemaining(observation -> assertArrayEquals(CONVERTED[i[0]++], observation.getFeatures(Double[]::new)));
		assertEquals(3, i[0]);
	}

	@Test
	public void remove() {
		Iterator<ObservationInterface<Double>> iterator = variableCalculatorProvider.iterator();
		iterator.next();
		iterator.remove();
		assertArrayEquals(CONVERTED[2], iterator.next().getFeatures(Double[]::new));

		iterator = variableCalculatorProvider.iterator();
		assertArrayEquals(CONVERTED[0], iterator.next().getFeatures(Double[]::new));
		assertArrayEquals(CONVERTED[2], iterator.next().getFeatures(Double[]::new));
		assertFalse(iterator.hasNext());

	}

	@Test
	public void multipleIterators() {
		final int[] i = {0};
		variableCalculatorProvider.forEach(observation -> {
			assertArrayEquals(CONVERTED[i[0]++], observation.getFeatures(Double[]::new));
			final int[] inner = {0};
			variableCalculatorProvider.forEach(
					innerObs -> assertArrayEquals(CONVERTED[inner[0]++], innerObs.getFeatures(Double[]::new)));
		});

		assertEquals(3, i[0]);
	}
}
