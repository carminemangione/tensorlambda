package com.mangione.continuous.util.coersion;

import java.util.Arrays;
import java.util.List;

public class CoerceToIntArray {
	public static int[] coerce(List<? extends Number> list) {
		return list.stream().mapToInt(Number::intValue).toArray();
	}
	public static Integer[] coerce(int[] array) {
		return Arrays.stream(array).boxed().toArray(Integer[]::new);
	}
	public static int[] coerce(double[] array) {
		return Arrays.stream(array)
				.boxed()
				.map(Double::intValue)
				.mapToInt(Integer::intValue)
				.toArray();
	}

	public static Integer[] coerceToInteger(double[] array) {
		return Arrays.stream(array).boxed().map(Double::intValue).toArray(Integer[]::new);
	}

	public static Integer[] coerceToInteger(int[] array) {
		return Arrays.stream(array).boxed().toArray(Integer[]::new);
	}

	public static int[] coerce(Integer[] ints) {
		return Arrays.stream(ints).mapToInt(Integer::intValue).toArray();
	}
}

