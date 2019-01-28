package com.mangione.continuous.observationproviders;

import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ProxyValues;

@SuppressWarnings("ALL")
public class LabeledExemplarProvider<S, T, U extends ExemplarInterface<S, T>> extends LabeledObservationProvider<S, U> {

	private final ProxyValues targetProxy;

	public LabeledExemplarProvider(ObservationProviderInterface<S, U> wrappedProvider, ProxyValues columnNames, ProxyValues targetProxy) {
		super(wrappedProvider, columnNames);
		this.targetProxy = targetProxy;
	}

	public ProxyValues getTargetProxies() {
		return targetProxy;
	}
}
