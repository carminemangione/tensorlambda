package com.mangione.continuous.calculators.zscale;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;

public class ColumnZScale {

    private final double mean;
    private final double standardDeviation;

    ColumnZScale(double[] columnVector) {
        DescriptiveStatistics ds = new DescriptiveStatistics(columnVector);
        mean = ds.getMean();
        standardDeviation = ds.getStandardDeviation();
    }

    ColumnZScale(List<Double> columnVector) {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        columnVector.forEach(ds::addValue);
        mean = ds.getMean();
        standardDeviation = ds.getStandardDeviation();
    }

    public double zscale(double value) {
        return (value - mean) / standardDeviation;
    }
}
