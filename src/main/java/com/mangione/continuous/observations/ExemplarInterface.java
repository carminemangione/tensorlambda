package com.mangione.continuous.observations;

public interface ExemplarInterface <S, T> extends ObservationInterface<S> {
	T getTarget();
	int getTargetIndex();
}
