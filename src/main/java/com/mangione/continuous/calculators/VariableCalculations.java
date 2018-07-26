package com.mangione.continuous.calculators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

		final List<S> translatedVariables = new ArrayList<>();

		features.stream()
				.map(x -> calculateVariableWithIndexedCalculatorOrDefault(x, i.getAndIncrement(), features))
				.forEach(translatedVariables::addAll);

		return translatedVariables;
	}

	public  VariableCalculator<R, S> getCalculator(int index) {
		final VariableCalculator<R, S> variableCalculator = indexToCalculator.get(index);
		return variableCalculator !=null ? variableCalculator : defaultCalculator;
	}

	private List<S> calculateVariableWithIndexedCalculatorOrDefault(R variable, int index, List<R> feature) {
		return indexToCalculator.get(index) != null ?
				indexToCalculator.get(index).apply(variable, feature) :
				//defaultCalculator.apply(variable, feature);
				new ArrayList<>();
	}


}
