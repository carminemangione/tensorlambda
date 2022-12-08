package com.mangione.continuous.performance.confusionmatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class BinnedPredictionPoints {
	private final double rangePerBin;
	private final List<List<PredictionPoint>> binnedPredictionPoints;
	private final List<PredictionPoint> points;
	private double currentMin;

	BinnedPredictionPoints(List<PredictionPoint> points, int numBins) {
		this.points = new ArrayList<>(points);
		Collections.sort(this.points);
		double maxScore = this.points.get(0).getThreshold();
		double minScore = this.points.get(points.size() - 1).getThreshold();
		rangePerBin = (maxScore - minScore) / numBins;
		currentMin = maxScore;
		binnedPredictionPoints = IntStream.range(0, numBins)
				.boxed()
				.map(this::processPoints)
				.collect(Collectors.toList());
	}

	List<List<PredictionPoint>> getBinnedPredictionPoints() {
		return binnedPredictionPoints;
	}

	private List<PredictionPoint> processPoints(int curBinNumber) {
		List<PredictionPoint> predictionPoints;
		double newMin = currentMin - rangePerBin;
		predictionPoints = points.stream()
				.filter(point -> point.getThreshold() >= newMin)
				.collect(Collectors.toList());
		points.removeAll(predictionPoints);
		this.currentMin = newMin;
		return predictionPoints;
	}

}
