package com.mangione.continuous.sampling;

import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SamplingWithoutReplacementTest {

    @Test
    public void exactlyMatchPercentOnFirstDraw() throws Exception {
        double[]  expectedToPickTheFirstAndThird = {0.09, 0.10, 0.0999999999};

        final MockRandom random = new MockRandom(expectedToPickTheFirstAndThird);
        final double samplePercent = 0.10;
        assertTrue(new SamplingWithoutReplacement(samplePercent, 10, random).select());
        assertFalse(new SamplingWithoutReplacement(samplePercent, 10, random).select());
        assertTrue(new SamplingWithoutReplacement(samplePercent, 10, random).select());
    }

    @Test
    public void adjustmentMadeOnSecondDraw() throws Exception {
        double[]  expectedToPickTheFirstAndSecond = {0.09, 0.00999};

        final MockRandom random = new MockRandom(expectedToPickTheFirstAndSecond);
        final double samplePercent = 0.10;
        final int totalNumber = 10;

        final SamplingWithoutReplacement samplingWithoutReplacement = new SamplingWithoutReplacement(samplePercent, totalNumber, random);
        assertTrue(samplingWithoutReplacement.select());
        assertFalse(samplingWithoutReplacement.select());
    }



    private class MockRandom implements RandomGenerator {
        private final double[] doubles;
        private int index;

        public MockRandom(double[] doubles) {
            this.doubles = doubles;
        }

        @Override
        public void setSeed(int i) {
        }

        @Override
        public void setSeed(int[] ints) {

        }

        @Override
        public void setSeed(long l) {

        }

        @Override
        public void nextBytes(byte[] bytes) {

        }

        @Override
        public int nextInt() {
            return 0;
        }

        @Override
        public int nextInt(int i) {
            return 0;
        }

        @Override
        public long nextLong() {
            return 0;
        }

        @Override
        public boolean nextBoolean() {
            return false;
        }

        @Override
        public float nextFloat() {
            return 0;
        }

        @Override
        public double nextDouble() {
            return doubles[index++];
        }

        @Override
        public double nextGaussian() {
            return 0;
        }
    }

}