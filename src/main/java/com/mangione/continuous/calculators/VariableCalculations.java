package com.mangione.continuous.calculators;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class VariableCalculations<R, S> implements Serializable {
	private static final long serialVersionUID = -2819411644387468769L;
	private final Map<Integer, ? extends VariableCalculator<R, S>> indexToCalculator;
	private final VariableCalculator<R, S> defaultCalculator;

	public VariableCalculations(Map<Integer, ? extends VariableCalculator<R, S>> indexToCalculator,
			VariableCalculator<R, S> defaultCalculator) {
		this.indexToCalculator = indexToCalculator;
		this.defaultCalculator = defaultCalculator;
	}

	public List<S> translateAllVariables(List<R> features) {
		AtomicInteger i = new AtomicInteger();

		return features.stream()
				.map(x -> calculateVariableWithIndexedCalculatorOrDefault(x, i.getAndIncrement(), features))
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	private List<S> calculateVariableWithIndexedCalculatorOrDefault(R variable, int index, List<R> feature) {
		return indexToCalculator.get(index) != null ?
				indexToCalculator.get(index).apply(variable, feature) :
				defaultCalculator.apply(variable, feature);
	}


}
