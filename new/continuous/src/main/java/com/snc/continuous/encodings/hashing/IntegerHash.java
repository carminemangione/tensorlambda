package com.mangione.continuous.encodings.hashing;

import java.util.function.Function;

import org.apache.commons.codec.digest.MurmurHash3;


class IntegerHash implements Function<Integer, Integer> {
	private final int seed;
	IntegerHash(int seed) {
		this.seed = seed;
	}

	@Override
	public Integer apply(Integer integer) {
		return MurmurHash3.hash32(integer, seed);
	}
}
