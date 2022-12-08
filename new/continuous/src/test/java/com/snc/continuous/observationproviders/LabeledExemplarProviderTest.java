package com.mangione.continuous.observationproviders;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mangione.continuous.encodings.ProxyValues;
import com.mangione.continuous.observations.dense.DiscreteExemplar;

public class LabeledExemplarProviderTest {

	@Test
	public void getTargetLabel() {
		ProxyValues columnNamesProxy = new ProxyValues();
		Arrays.stream(new String[]{"Hello", "There", "Boys", "!"}).forEach(columnNamesProxy::add);

		ProxyValues targetProxy = new ProxyValues();
		Arrays.stream(new String[]{"True", "False"}).forEach(targetProxy::add);

		ArrayObservationProvider<Integer, DiscreteExemplar> aop =
				new ArrayObservationProvider<>(ArrayObservationProvider.integerFromPrimitive(new int[4][1]),
						integers -> new DiscreteExemplar(Arrays.asList(integers), 1));

		LabeledExemplarProvider<Integer, Integer, DiscreteExemplar> discreteExemplars =
				new LabeledExemplarProvider<>(aop, columnNamesProxy, targetProxy);

		assertEquals("True", discreteExemplars.getTargetProxies().getName(0));
		assertEquals("False", discreteExemplars.getTargetProxies().getName(1));
	}
}