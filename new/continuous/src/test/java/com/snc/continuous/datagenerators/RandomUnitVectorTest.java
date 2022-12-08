package com.mangione.continuous.datagenerators;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RandomUnitVectorTest {
    @Test
    public void createUnitVector() throws Exception {
        int numberOfDimensions = 100;
        RandomGenerator generator = new MersenneTwister(873264);
        RandomUnitVector ruv = new RandomUnitVector(numberOfDimensions, generator);
        for (int i = 0; i < 100; i++) {
            double[] unitVector = ruv.nextVector();
            assertEquals(numberOfDimensions, unitVector.length);
            assertEquals(1.0, new ArrayRealVector(unitVector).getNorm(), 0.00001);
        }
    }
}