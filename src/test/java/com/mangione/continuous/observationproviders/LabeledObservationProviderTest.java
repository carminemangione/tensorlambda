package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.dense.Observation;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.mangione.continuous.observationproviders.ArrayObservationProvider.doubleFromPrimitive;
import static org.junit.Assert.assertEquals;

public class LabeledObservationProviderTest {

	private static final Double[][] DATA = new Double[4][4];

	private ArrayObservationProvider<Double, ObservationInterface<Double>> aop;

	@Before
	public void setUp() {
		aop = new ArrayObservationProvider<>(DATA,
				doubles -> new Observation<>(Arrays.asList(doubles)));
	}

	@Test
	public void getColumnLabels() {
		String[] columnNames = {"Hello", "There", "Boys", "!"};
		ProxyValues columnNamesProxy = new ProxyValues();
		Arrays.stream(columnNames).forEach(columnNamesProxy::add);

		aop = new ArrayObservationProvider<>(doubleFromPrimitive(new double[4][1]), doubles -> new Observation<>(Arrays.asList(doubles)));
		LabeledObservationProvider<Double, ObservationInterface<Double>> labeledProvider =
				new LabeledObservationProvider<>(aop, columnNamesProxy);

		ProxyValues proxies = labeledProvider.getNamedColumns();
		for (int i = 0; i < columnNames.length; i++) {
			assertEquals(columnNames[i], proxies.getName(i));
		}
	}

}