package com.mangione.continuous.encodings.random;

import java.util.Random;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class JumpWRSampler extends WRSampler {
    public JumpWRSampler(int numberOfChoices, int size, Random random) {
        super(numberOfChoices, size, random);
    }

    public int[] nextExponentialJump() {
        // Fill reservoir.
        TreeMap<Double, Integer> reservoir = new TreeMap<>();
        IntStream.range(0, numberChosen).forEach(x -> reservoir.put(uniform(), x));
        int v = numberChosen;
        double tw = reservoir.firstKey();
        //Skip through the sequence V.
        while (v < size) {
            v += randomJump(tw);
            reservoir.remove(reservoir.firstKey());
            reservoir.put(uniform(tw, 1.0), v);
            tw = reservoir.firstKey();
        }
        //Pack up and go home.
        return reservoir
                .values()
                .stream()
                .sorted(Integer::compareTo)
                .mapToInt(Integer::valueOf)
                .toArray();
    }

    private int randomJump(double tw) {
        return (int) Math.ceil(Math.log(uniform()) / Math.log(tw));
    }

    @Override
    public int[] next(int numberChosen) {
        this.numberChosen = numberChosen;
        return next();
    }

}
