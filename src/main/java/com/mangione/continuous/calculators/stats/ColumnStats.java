package com.mangione.continuous.calculators.stats;

import java.io.Serializable;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class ColumnStats implements Serializable {

	private static final long serialVersionUID = 7889775836422189051L;
	private final double avg;
	private final double max;
	private final double min;
	private final double std;

	private ColumnStats(DescriptiveStatistics stats) {

		avg = stats.getMean();
		max = stats.getMax();
		min = stats.getMin();
		std = stats.getStandardDeviation();
	}

	public double avg() {
		return avg;
	}

	public double max() {
		return max;
	}

	public double min() {
		return min;
	}

	public double std() {
		return std;
	}

	public static class Builder {
		DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();

		public void add(double value) {
			descriptiveStatistics.addValue(value);
		}

		public ColumnStats build() {
			return new ColumnStats(descriptiveStatistics);
		}
	}

}

