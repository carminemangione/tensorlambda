package com.mangione.continuous.calculators.stats;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.random.EmpiricalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class ColumnStats implements Serializable {

	private static final long serialVersionUID = 7889775836422189051L;
	private final double avg;
	private final double max;
	private final double min;
	private final double std;
	private final long[] histogram;

	private ColumnStats(DescriptiveStatistics stats, long[] histogram) {

		avg = stats.getMean();
		max = stats.getMax();
		min = stats.getMin();
		std = stats.getStandardDeviation();
		this.histogram = histogram;
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

	public long[] getHistogram() {
		return histogram;
	}

	public static class Builder {

		private final DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
		private final EmpiricalDistribution empiricalDistribution;
		private final int numberOfBins;

		public Builder(int numberOfBins) {
			empiricalDistribution = new EmpiricalDistribution(numberOfBins);
			this.numberOfBins = numberOfBins;
		}

		public void add(double value) {
			descriptiveStatistics.addValue(value);
		}

		public ColumnStats build() {

			long[] histogram = ArrayUtils.toPrimitive(empiricalDistribution.getBinStats()
					.stream()
					.map(SummaryStatistics::getN).toArray(Long[]::new));

			return new ColumnStats(descriptiveStatistics, histogram);
		}
	}

}

