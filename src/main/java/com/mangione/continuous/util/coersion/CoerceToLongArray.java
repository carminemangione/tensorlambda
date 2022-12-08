package com.mangione.continuous.util.coersion;

import java.util.Arrays;
import java.util.List;

public class CoerceToLongArray {
	public static long[] coerce(int[] array) {
		return Arrays.stream(array).boxed().mapToLong(Integer::longValue).toArray();
	}

	public static long[] coerce(List<? extends Number> values) {
		return values.stream().mapToLong(Number::longValue).toArray();
	}
}
