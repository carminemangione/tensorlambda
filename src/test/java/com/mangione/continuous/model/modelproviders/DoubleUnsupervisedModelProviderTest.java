package com.mangione.continuous.model.modelproviders;

import static java.util.Arrays.stream;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observations.DoubleObservationFactory;
import com.mangione.continuous.observations.ObservationInterface;

public class DoubleUnsupervisedModelProviderTest {

	@Test
	public void testFilling2DArray() throws Exception {
		Double[][] data = {{1., 2., 3.}, {4., 5., 6.}};
		ArrayObservationProvider<Double, ObservationInterface<Double>> arrayObservationProvider = new ArrayObservationProvider<>(data, new DoubleObservationFactory());

		DoubleUnsupervisedModelProvider provider = new DoubleUnsupervisedModelProvider(arrayObservationProvider);

		assertEquals(2, provider.getNumberOfLines());


		double[][] doubleData = stream(data)
				.map(row -> stream(row)
						.mapToDouble(Number::doubleValue).toArray())
				.toArray(double[][]::new);

		assertArrayEquals(doubleData, data);
	}


}