package com.mangione.continuous.calculators.zscale;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ColumnZScaleTest {

    @Test
    public void twoNumbers() {
        double[] columnVectors = {4, 16};
        ColumnZScale czs = new ColumnZScale(columnVectors);
        double average = 10.0;
        double sd = 8.48528;
        double test = 8754.98;

        assertEquals((test - average) / sd, czs.zscale(test), 0.001);
    }

    @Test
    public void twoNumbersList() {
        ColumnZScale czs = new ColumnZScale(Arrays.asList(4., 16.));
        double average = 10.0;
        double sd = 8.48528;
        double test = 8754.98;

        assertEquals((test - average) / sd, czs.zscale(test), 0.001);
    }

}