package com.mangione.continuous.calculators.stats;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.random.EmpiricalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;

@SuppressWarnings("WeakerAccess")
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

	@Override
	public String toString() {
		return "ColumnStats{" +
				"avg=" + avg +
				", max=" + max +
				", min=" + min +
				", std=" + std +
				", histogram=" + Arrays.toString(histogram) +
				'}';
	}

	public static class Builder {

		private final DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
		private final EmpiricalDistribution empiricalDistribution;

		public Builder(int numberOfBins) {
			empiricalDistribution = new EmpiricalDistribution(numberOfBins);
		}

		public void add(double value) {
			descriptiveStatistics.addValue(value);
		}

		public ColumnStats build() {
			empiricalDistribution.load(descriptiveStatistics.getValues());

			long[] histogram = ArrayUtils.toPrimitive(empiricalDistribution.getBinStats()
					.stream()
					.map(SummaryStatistics::getN).toArray(Long[]::new));

			return new ColumnStats(descriptiveStatistics, histogram);
		}
	}

}

