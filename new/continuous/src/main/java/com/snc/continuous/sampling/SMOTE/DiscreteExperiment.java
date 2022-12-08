package com.mangione.continuous.sampling.SMOTE;

import org.opencompare.hac.experiment.Experiment;

import com.mangione.continuous.observationproviders.ListObservationProvider;
import com.mangione.continuous.observations.ObservationInterface;

public class DiscreteExperiment<PROVIDER extends ListObservationProvider<Integer, ? extends ObservationInterface<Integer>>> implements Experiment {
	private final PROVIDER provider;

	public DiscreteExperiment(PROVIDER provider) {
		this.provider = provider;
	}

	public PROVIDER getProvider() {
		return provider;
	}

	@Override
	public int getNumberOfObservations() {
		return (int)provider.size();
	}
}
