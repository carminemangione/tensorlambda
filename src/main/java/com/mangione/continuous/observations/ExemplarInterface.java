package com.mangione.continuous.observations;

import java.util.List;

public interface ExemplarInterface <S, T> extends ObservationInterface<S> {

	List<S> getExemplar();

	T getTarget();
}
