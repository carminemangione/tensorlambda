package com.mangione.continuous.observations;

import java.util.ArrayList;
import java.util.List;

public class DiscreteExemplarFactory implements ExemplarFactoryInterface<Double, ExemplarInterface<Double, Integer>> {
	@Override
	public DiscreteExemplar<Double> create(List<Double> data) {
		final List<Double> doubles = new ArrayList<>(data);
		
		final Double target = doubles.get(data.size() - 1);
		return new DiscreteExemplar<>(doubles.subList(0, data.size() - 1), target, target.intValue());
	}


}
