package com.mangione.continuous.calculators;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

public class VariableCalculationsTest {
	@Test
	public void addListCalculator() {
		Function<Integer, List<Integer>> listFunction = integer -> Arrays.asList(integer + 10, integer + 20);
		VariableCalculations.Builder<Integer, Integer> builder = new VariableCalculations.Builder<>();
		VariableCalculations<Integer, Integer> calculations = builder.addListCalculator(0, listFunction).build();
		List<Integer> variables = calculations.translateAllVariables(new Integer[]{1});
		assertEquals(Arrays.asList(11, 21), variables);
	}

	@Test
	public void addCalculator() {
		Function<Integer, Integer> function = integer -> integer + 10;
		VariableCalculations.Builder<Integer, Integer> builder = new VariableCalculations.Builder<>();
		VariableCalculations<Integer, Integer> calculations = builder.addCalculator(0, function).build();
		List<Integer> variables = calculations.translateAllVariables(new Integer[]{1});
		assertEquals(Collections.singletonList(11), variables);
	}

	@Test
	public void useDefaultCalculator() {
		Function<Integer, Integer> function = integer -> integer + 10;
		VariableCalculations.Builder<Integer, Integer> builder = new VariableCalculations.Builder<>();
		VariableCalculations<Integer, Integer> calculations = builder.setDefaultCalculator(function).build();
		List<Integer> variables = calculations.translateAllVariables(new Integer[]{1});
		assertEquals(Collections.singletonList(11), variables);
	}

	@Test
	public void defaultCalculatorIsList() {
		Function<Integer, List<Integer>> listFunction = integer -> Arrays.asList(integer + 10, integer + 20);
		VariableCalculations.Builder<Integer, Integer> builder = new VariableCalculations.Builder<>();
		VariableCalculations<Integer, Integer> calculations = builder.setDefaultListCalculator(listFunction).build();
		List<Integer> variables = calculations.translateAllVariables(new Integer[]{1});
		assertEquals(Arrays.asList(11, 21), variables);
	}

	@Test
	public void noDefaultReturnsNull() {
		VariableCalculations.Builder<Integer, Integer> builder = new VariableCalculations.Builder<>();
		VariableCalculations<Integer, Integer> calculations = builder.build();
		List<Integer> variables = calculations.translateAllVariables(new Integer[]{1});
		assertNull(variables.get(0));
	}

	@Test
	public void listAndSingleValueWithDefault() {
		Function<Integer, List<Integer>> listFunction = integer -> Arrays.asList(integer + 10, integer + 20);
		Function<Integer, Integer> defaultFunction = integer -> integer + 30;
		Function<Integer, Integer> function = integer -> integer + 40;

		VariableCalculations.Builder<Integer, Integer> builder = new VariableCalculations.Builder<>();
		VariableCalculations<Integer, Integer> calculations = builder
				.addListCalculator(0, listFunction)
				.addCalculator(1, function)
				.setDefaultCalculator(defaultFunction)
				.build();
		List<Integer> variables = calculations.translateAllVariables(new Integer[] {1, 2, 3});
		assertEquals(Arrays.asList(11, 21, 42, 33), variables);
	}

	@Test(expected = IllegalStateException.class)
	public void onlyOneDefaultAllowedExceptsWhenListAddedSecond() {
		Function<Integer, List<Integer>> defaultListFunction = integer -> Arrays.asList(integer + 10, integer + 20);
		Function<Integer, Integer> defaultFunction = integer -> integer + 30;
		new VariableCalculations.Builder<Integer, Integer>()
				.setDefaultCalculator(defaultFunction)
				.setDefaultListCalculator(defaultListFunction);
	}

	@Test(expected = IllegalStateException.class)
	public void onlyOneDefaultAllowedExceptsWhenDefaultAddedSecond() {
		Function<Integer, List<Integer>> defaultListFunction = integer -> Arrays.asList(integer + 10, integer + 20);
		Function<Integer, Integer> defaultFunction = integer -> integer + 30;
		new VariableCalculations.Builder<Integer, Integer>()
				.setDefaultListCalculator(defaultListFunction)
				.setDefaultCalculator(defaultFunction);
	}
}