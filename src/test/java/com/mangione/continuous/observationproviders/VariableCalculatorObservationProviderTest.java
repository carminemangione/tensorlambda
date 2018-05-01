package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationInterface;

public class VariableCalculatorObservationProviderTest {
	@Test
	public void variableCalculator() throws Exception {

		Map<Integer, VariableCalculator<String, Double>> calculators = new HashMap<>();
		calculators.put(0, feature -> {
			Double[] out = new Double[]{0d,0d,0d};
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

		ArrayObservationProvider<String, ObservationInterface<String>> arrayObservationProvider =
				new ArrayObservationProvider<String, ObservationInterface<String>>(new String[][]{{"a"}, {"b"}, {"c"}},
				Observation::new);
		VariableCalculatorObservationProvider<String, Double, ObservationInterface<Double>> op = new VariableCalculatorObservationProvider<>(arrayObservationProvider,
				new StringToDoubleVariableCalculator(), calculators, new DoubleArraySupplier(), new DoubleObservationFactory<>());


		assertEquals(1d, op.next().getFeatures()[0], 0);
		assertEquals(1d, op.next().getFeatures()[1], 0);
		assertEquals(1d, op.next().getFeatures()[2], 0);

	}

}