package com.mangione.continuous.encodings.random;

import java.util.Random;

public class FastRareBinomial extends Binomial {
	public FastRareBinomial(double p, int trials, Random random) {
		super(p, trials, random);
	}

	@Override
	public Integer next() {
		double q = -Math.log(1 - p);
		int x = 0;
		double sum = 0.0;
		do {
			double e = exponential();
			if (x == trials)
				sum = Double.MAX_VALUE;
			else
				sum += e / (trials - x);
			x += 1;
		} while (sum <= q);
		x -= 1;
		return x;
	}
}
