package com.mangione.continuous.util;

import org.apache.commons.codec.digest.MurmurHash3;


@SuppressWarnings("WeakerAccess")
public class MurmurHash3Random {
	private final long seed;

	public MurmurHash3Random(long seed) {
		this.seed = seed;
	}

	public double nextDouble(long num) {
		long hash = Math.abs(MurmurHash3.hash64(num | seed));
		return ((double) Long.MAX_VALUE - hash) / Long.MAX_VALUE;
	}
}
