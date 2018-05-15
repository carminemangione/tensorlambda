package com.mangione.continuous.model;

import java.io.Serializable;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ObservationInterface;

public interface SupervisedModelInterace<R, S, T extends ExemplarInterface<R, S>> extends Serializable {
	void train(ObservationProviderInterface<R, T> provider);
	double score(ObservationInterface<R> observation);
}
