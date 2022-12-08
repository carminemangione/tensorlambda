package com.mangione.continuous.performance.confusionmatrix;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mangione.continuous.performance.ROCInterface;
import com.mangione.continuous.performance.RocBuilderInterface;

public class BinaryROCConfusionMatrix implements ROCInterface<ROCConfusionMatrixPoint> {

	private final List<ROCConfusionMatrixPoint> roc;

	public BinaryROCConfusionMatrix(List<ConfusionMatrix> matrices) {
		roc = matrices.stream()
				.map(ROCConfusionMatrixPoint::new)
				.collect(Collectors.toList());
	}

	@Override
	public List<ROCConfusionMatrixPoint> getROC() {
		return roc;
	}

	public static class Builder implements RocBuilderInterface<ROCConfusionMatrixPoint> {
		private final List<PredictionPoint> points = new LinkedList<>();
		private final int numBins;

		public Builder(int numBins) {
			this.numBins = numBins;
		}

		@Override
		public RocBuilderInterface<ROCConfusionMatrixPoint> add(double threshold, Integer actual, Integer predicted) {
			points.add(new PredictionPoint(threshold, actual, predicted));
			return this;
		}

		@Override
		public ROCInterface<ROCConfusionMatrixPoint> build() {
			List<ConfusionMatrix> matrices = new BinnedPredictionPoints(points, numBins).getBinnedPredictionPoints().stream()
					.map(this::createConfusionMatrix)
					.collect(Collectors.toList());
			return new BinaryROCConfusionMatrix(matrices);
		}

		private ConfusionMatrix createConfusionMatrix(List<PredictionPoint> points) {
			ConfusionMatrix.Builder builder = new ConfusionMatrix.Builder(points.get(0).getThreshold());
			points.forEach(point->builder.add(point.getPredicted(), point.getActual()));
			return builder.build();
		}

	}
}
