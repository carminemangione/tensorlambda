package com.mangione.continuous.encodings.random;

import org.junit.Test;

import java.util.Random;

public class BinomialTest {
    int seed =1305385;
    int repetitions = 1000;
    @Test
    public void test(){
        double p = 1.0/1024;
        int trials = 8192;
        Binomial b = new Binomial(p, trials, new Random(seed));
        double popP = 0.0;
        long startTime = System.nanoTime();
        for(int i = 0; i< repetitions; i++){
            popP+=b.next();
        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
        popP/= repetitions*trials;
        double relErr =(popP- p)/p;
        System.out.println(relErr);
    }

    @Test
    public void fastTest(){
        double p = 1.0/1024;
        int trials = 8192;
        Binomial b = new FastRareBinomial(p, trials, new Random(seed));
        double popP = 0.0;
        long startTime = System.nanoTime();
        for(int i = 0; i< repetitions; i++){
            popP+=b.next();
        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
        popP/= repetitions*trials;
        double relErr =(popP- p)/p;
        System.out.println((popP- p)/p);
    }
}
