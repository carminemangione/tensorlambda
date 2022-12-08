package com.mangione.continuous.encodings.random;

import java.util.Random;

public class Binomial extends Generator<Integer> {
    final double p;
    final int trials;

    public Binomial(double p, int trials, Random random){
        super(random);
        this.p = p;
        this.trials = trials;
    }

    @Override
    public Integer next(){
        int sum = 0;
        for(int t = 0; t < trials; t++) sum += Bernoulli(p);
        return sum;
    }
}
