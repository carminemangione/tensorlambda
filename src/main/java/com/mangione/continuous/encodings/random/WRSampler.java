package com.mangione.continuous.encodings.random;

import java.util.HashSet;
import java.util.Random;

public class WRSampler extends Generator<int[]> {
    int numberChosen;
    final int size;

    public WRSampler(int numberOfChoices, int size, Random random) {
        super(random);
        this.numberChosen = numberOfChoices;
        this.size = size;
    }

    @Override
    public int[] next() {
        HashSet<Integer> chosen = new HashSet<>();
        do {
            int choice;
            do {
                choice = uniform(size);
            } while (chosen.contains(choice));
            chosen.add(choice);
        } while (chosen.size() < numberChosen);
        return chosen.stream().sorted().mapToInt(Integer::intValue).toArray();
    }

    public int[] next(int numberChosen) {
        this.numberChosen = numberChosen;
        return next();
    }
}
