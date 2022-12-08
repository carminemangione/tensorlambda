package com.mangione.continuous.encodings.johnson_lindenstrauss;

import java.util.Random;

import com.mangione.continuous.encodings.random.FastRareBinomial;
import com.mangione.continuous.encodings.random.WRSampler;
import com.mangione.continuous.linalg.matrix.MatrixRelation;

public class JLTransformBuilder {
	private final int d;
	private final int k;
	private final double q;
	private final Random rng;

	public JLTransformBuilder(int d, int k, double q, Random rng) {
		this.d = d;
		this.k = k;
		this.q = q;
		this.rng = rng;
	}

	SimpleJLTransform createSimpleJLTransform(int d0) {
		return new SimpleJLTransform(d0, d, k, createRandomDiagonal(), createRandomProjetion());
	}

	private MatrixRelation createRandomProjetion() {
		MatrixRelation relation = new MatrixRelation(k, d);
		double sigma = Math.sqrt(1.0 / q);
		FastRareBinomial binomial = new FastRareBinomial(this.q, k * d, rng);
		WRSampler sampler = new WRSampler(binomial.next(), k * d, rng);
		int[] nonZeros = sampler.next();
		for (int i : nonZeros) {
			relation.add(i / d, i % d, rng.nextGaussian() * sigma);
		}
		return relation;
	}

	public int getD() {
		return d;
	}

	public int getK() {
		return k;
	}

	public double getQ() {
		return q;
	}

	private double[] createRandomDiagonal() {
		double[] randomDiagonal = new double[d];
		for (int i = 0; i < d; i++) {
			randomDiagonal[i] = rng.nextDouble() > 0.5 ? 1.0 : -1.0;
		}
		return randomDiagonal;
	}
}
