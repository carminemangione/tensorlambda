package com.mangione.continuous.performance;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.classifiers.supervised.smile.TagToIndex;

public class CumulativeProbabilityFromClassification {
	private static final Logger LOGGER = LoggerFactory.getLogger(CumulativeProbabilityFromClassification.class);

	private final TagToIndex tagToIndex;
	private final double[] cumulativeProbability;
	private int numPredictions;

	public CumulativeProbabilityFromClassification(TagToIndex tagToIndex) {
		this.tagToIndex = tagToIndex;
		this.cumulativeProbability = new double[tagToIndex.size()];
	}

	public void addPrediction(int actual, double[] apriori) {
		ScoreRank[] sortedScoreRanks = IntStream.range(0, apriori.length)
				.boxed()
				.map(index -> new ScoreRank(apriori[index], index))
				.sorted()
				.toArray(ScoreRank[]::new);
		numPredictions++;

		Integer indexOfActualTag = tagToIndex.getIndex(actual);
		if (indexOfActualTag != null) {
			int fromIndex = numNeededToCaptureActual(sortedScoreRanks, indexOfActualTag);
			for (int i = fromIndex; i < cumulativeProbability.length; i++) {
				cumulativeProbability[i]++;
			}
		} else {
			LOGGER.warn("Tag index not found: " + actual);
		}
	}

	public double[] buildCumulativeProbabilities() {
		return Arrays.stream(cumulativeProbability)
				.boxed()
				.mapToDouble(x -> x/numPredictions)
				.toArray();
	}

	private int numNeededToCaptureActual(ScoreRank[] sortedScoreRanks,  int indexOfActualTag) {
		Optional<Integer> locationOfActualIndex = IntStream.range(0, sortedScoreRanks.length)
				.boxed()
				.filter(i -> sortedScoreRanks[i].getOriginalIndex() == indexOfActualTag)
				.findFirst();
		//noinspection OptionalGetWithoutIsPresent
		return locationOfActualIndex.get();
	}

	private static class ScoreRank implements Comparable<ScoreRank> {
		private final double score;
		private final int originalIndex;

		private ScoreRank(double score, int originalIndex) {
			this.score = score;
			this.originalIndex = originalIndex;
		}

		public int getOriginalIndex() {
			return originalIndex;
		}

		@Override
		public int compareTo(@Nonnull CumulativeProbabilityFromClassification.ScoreRank o) {
			return Double.compare(o.score, score);
		}

		@Override
		public String toString() {
			return "ScoreRank{" +
					"score=" + score +
					", originalIndex=" + originalIndex +
					'}';
		}
	}
}
