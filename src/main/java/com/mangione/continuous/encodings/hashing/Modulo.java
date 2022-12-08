package com.mangione.continuous.encodings.hashing;

import java.util.function.Function;

public class Modulo implements Function<Integer, Integer> {
	private final int base;

	Modulo(int base) {
		this.base = base;
	}

	@Override
	public Integer apply(Integer integer) {
		return Integer.remainderUnsigned(integer, base);
	}
}
