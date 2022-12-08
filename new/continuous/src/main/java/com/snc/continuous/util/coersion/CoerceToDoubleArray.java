package com.mangione.continuous.util.coersion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CoerceToDoubleArray {
	public static double[] coerce(List<? extends Number> list) {
		return list.stream().mapToDouble(Number::doubleValue).toArray();
	}

	public static double[] coerce(int[] vec) {
		return Arrays.stream(vec)
				.boxed()
				.mapToDouble(Integer::doubleValue)
				.toArray();
	}

	public static double[][] coerce(int[][] matrix) {
		double[][] converted = new double[matrix.length][];
		IntStream.range(0, matrix.length)
				.boxed()
				.forEach(i-> converted[i] = coerce(matrix[i]));
		return converted;
	}

	public static Double[] coerce(double[] vec) {
		return Arrays.stream(vec).boxed().toArray(Double[]::new);
	}

	public static double[] coerce(Number[] vec) {
		return Arrays.stream(vec).mapToDouble(Number::doubleValue).toArray();
	}
}
