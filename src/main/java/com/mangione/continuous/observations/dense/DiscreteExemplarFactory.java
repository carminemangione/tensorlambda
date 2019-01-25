package com.mangione.continuous.observations.dense;

import java.util.ArrayList;
import java.util.List;

import com.mangione.continuous.observations.ExemplarFactoryInterface;

public class DiscreteExemplarFactory<S extends Number> implements ExemplarFactoryInterface<S, DiscreteExemplar<S>> {
	@Override
	public DiscreteExemplar<S> create(List<S> data) {
		final List<S> doubles = new ArrayList<>(data);
		
		final S target = doubles.get(data.size() - 1);
		return new DiscreteExemplar<>(doubles.subList(0, data.size() - 1), target, target.intValue());
	}
}
