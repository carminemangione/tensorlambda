package com.mangione.continuous.encodings;

import java.util.Arrays;

public class ZeroOneEncoder {
	private final int numCols;

	public ZeroOneEncoder(int numCols) {
		this.numCols = numCols;
	}

	public double[] encode(int... indexes) {
		double[] encoded = new double[numCols];
		Arrays.stream(indexes).forEach(index->encoded[index] = 1);
		return encoded;
	}
}
