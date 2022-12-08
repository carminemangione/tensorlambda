package com.mangione.continuous.calculators;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariableCalculations<R, S> implements Serializable {
	private static final long serialVersionUID = -2819411644387468769L;

	private static final Logger LOGGER = LoggerFactory.getLogger(VariableCalculations.class);
	private final Map<Integer, Function<R, List<S>>> indexToListCalculator;
	private final Map<Integer, Function<R, S>> indexToCalculator;
	private final Function<R, List<S>> defaultListCalculator;
	private final Function<R, S> defaultCalculator;


	private VariableCalculations(Map<Integer, Function<R, List<S>>> indexToListCalculator,
			Map<Integer, Function<R, S>> indexToCalculator, Function<R, List<S>> defaultListCalculator, Function<R, S> defaultCalculator) {
		this.indexToCalculator = indexToCalculator;
		this.indexToListCalculator = indexToListCalculator;
		this.defaultListCalculator = defaultListCalculator;
		this.defaultCalculator = defaultCalculator;
	}

	List<S> translateAllVariables(R[] features) {
		AtomicInteger i = new AtomicInteger();

		try {
			return Arrays.stream(features)
					.map(x -> calculateVariableWithIndexedCalculatorOrDefault(x, i.getAndIncrement()))
					.flatMap(List::stream)
					.collect(Collectors.toList());
		} catch (Exception e) {
			LOGGER.error("Could not process features: " + Arrays.toString(features));
			throw e;
		}
	}

	private List<S> calculateVariableWithIndexedCalculatorOrDefault(R variable, int index) {
		return indexToListCalculator.get(index) != null ?
				indexToListCalculator.get(index).apply(variable) :
				indexToCalculator.get(index) != null ?
						Collections.singletonList(indexToCalculator.get(index).apply(variable)) :
						defaultListCalculator != null ?
								defaultListCalculator.apply(variable) :
								defaultCalculator != null ?
										Collections.singletonList(defaultCalculator.apply(variable))
										: Collections.singletonList(null);
	}

	public static class Builder<R, S> {
		private final Map<Integer, Function<R, List<S>>> indexToListCalculator = new HashMap<>();
		private final Map<Integer, Function<R, S>> indexToCalculator = new HashMap<>();
		private Function<R, List<S>> defaultListCalculator;
		private Function<R, S> defaultCalculator;

		public Builder<R, S> addListCalculator(int index, Function<R, List<S>> calculator) {
			indexToListCalculator.put(index, calculator);
			return this;
		}

		public Builder<R, S> addCalculator(int index, Function<R, S> calculator) {
			indexToCalculator.put(index, calculator);
			return this;
		}

		public VariableCalculations<R, S> build() {
			return new VariableCalculations<>(indexToListCalculator, indexToCalculator, defaultListCalculator, defaultCalculator);
		}

		public Builder<R, S> setDefaultListCalculator(Function<R, List<S>> defaultListCalculator) {
			if (defaultCalculator != null)
				throw new IllegalStateException("Default calculator already set, can not do both");
			this.defaultListCalculator = defaultListCalculator;
			return this;
		}

		public Builder<R, S> setDefaultCalculator(Function<R, S> defaultCalculator) {
			if (defaultListCalculator != null)
				throw new IllegalStateException("Default list calculator already set, can not do both");
			this.defaultCalculator = defaultCalculator;
			return this;
		}
	}
}
