package com.mangione.continuous.observationproviders;

import static com.mangione.continuous.observationproviders.ArrayObservationProvider.integerFromPrimitive;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mangione.continuous.observations.ProxyValues;
import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.dense.DiscreteExemplarFactory;

public class LabeledExemplarProviderTest {

	@Test
	public void getTargetLabel() {
		ProxyValues columnNamesProxy = new ProxyValues();
		Arrays.stream(new String[]{"Hello", "There", "Boys", "!"}).forEach(columnNamesProxy::add);

		ProxyValues targetProxy = new ProxyValues();
		Arrays.stream(new String[]{"True", "False"}).forEach(targetProxy::add);

		ArrayObservationProvider<Integer, DiscreteExemplar<Integer>> aop =
				new ArrayObservationProvider<>(integerFromPrimitive(new int[4][1]), new DiscreteExemplarFactory<>());

		LabeledExemplarProvider<Integer, Integer, DiscreteExemplar<Integer>> discreteExemplars =
				new LabeledExemplarProvider<>(aop, columnNamesProxy, targetProxy);

		assertEquals("True", discreteExemplars.getTargetProxies().getName(0));
		assertEquals("False", discreteExemplars.getTargetProxies().getName(1));
	}
}