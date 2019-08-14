package com.mangione.continuous.calculators;

import com.mangione.continuous.observations.ObservationInterface;

public interface KeyFactory<R,  T extends ObservationInterface<R>> {
	Object generateKey(T observation);
}
