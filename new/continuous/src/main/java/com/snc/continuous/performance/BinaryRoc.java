package com.mangione.continuous.performance;

import java.util.List;
import java.util.stream.Collectors;

public class BinaryRoc implements ROCInterface<ROCPoint> {
	private final List<ROCPoint> rocPoints;

	private BinaryRoc(List<ROCPoint> rocPoints) {
		this.rocPoints = rocPoints;
	}

	@Override
	public List<ROCPoint> getROC() {
		return rocPoints;
	}

	public static class Builder implements RocBuilderInterface<ROCPoint> {
		private final MultiCumulativeCounter.Builder<Integer> multiCumulativeCounter = new MultiCumulativeCounter.Builder<>();

		@Override
		public RocBuilderInterface<ROCPoint> add(double threshold, Integer actual, Integer predicted) {
			multiCumulativeCounter.add(threshold, actual);
			return this;
		}

		public ROCInterface<ROCPoint> build() {
			MultiCumulativeCounter<Integer> counter = multiCumulativeCounter.build();
			if (counter.getCounter(0) == null || counter.getCounter(1) == null)
				throw new IllegalStateException("Can not have empty scores for positive and negative");
			List<ROCPoint> rocPoints = counter.getAllCounter().getScores().stream()
					.map(threshold -> new ROCPoint(counter.getCounter(0).countLE(threshold),
							counter.getCounter(1).countLE(threshold),
							counter.getCounter(0).getTotalCount(),
							counter.getCounter(1).getTotalCount(),
							threshold))
					.sorted((p1,p2)-> Double.compare(p2.getThreshold(), p1.getThreshold()))
					.collect(Collectors.toList());
			return new BinaryRoc(rocPoints);
		}
	}
}
