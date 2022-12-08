package com.mangione.continuous.sampling.reservoir;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.AlternativeHypothesis;
import org.apache.commons.math3.stat.inference.BinomialTest;
import org.junit.Before;

class ReservoirSamplerWithoutReplacementTestParent {
	private Random random;

	@Before
	public void setup() {
		random = new Random(48736);
	}

	@Nonnull
	Map<Integer, Integer> generateDataAndSample(SamplerFactoryInterface<Item> factory, int totalNumberOfRecords, int sampleSize, double... classProbabilities) {
		List<Item> items = generateData(totalNumberOfRecords, classProbabilities);
		SamplerInterface<Item> sampler = factory.createSampler(items.iterator(),
				sampleSize, random.nextInt());

		List<Item> sample = sampler.sample();
		assertEquals(sampleSize, sample.size());

		return sample.stream()
				.collect(groupingBy(Item::getClassification, toList()))
				.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
	}


	private List<Item> generateData(double totalNumberOfRecords, double... classProbabilities) {
		List<Integer> classes = IntStream.range(0, classProbabilities.length)
				.boxed()
				.map(index -> (generateForClass(totalNumberOfRecords, classProbabilities[index], index)))
				.flatMap(Collection::stream)
				.collect(toList());
		Collections.shuffle(classes, random);

		return IntStream.range(0, classes.size())
				.boxed()
				.map(index -> new Item(index, classes.get(index)))
				.collect(Collectors.toList());
	}

	private List<Integer> generateForClass(double totalNumberOfRecords, double probability, int classification) {
		return IntStream.range(0, (int) (totalNumberOfRecords * probability))
				.map(x -> classification)
				.boxed()
				.collect(toList());
	}

	void validateResults(int sampleSize, double probSuccess, int successes) {
		double mu = probSuccess * sampleSize;
		double sigma = sqrt(probSuccess * (1 - probSuccess) * sampleSize);
		System.out.printf("mu: %12.5e sigma: %12.5e\n", mu, sigma);
		double z = (successes - mu) / sigma;
		NormalDistribution normalDistribution = new NormalDistribution(0, 1.0);
		double gaussianPValue = 1.0 - (normalDistribution.cumulativeProbability(abs(z)) - normalDistribution.cumulativeProbability(-abs(z)));
		BinomialTest binomialTest = new BinomialTest();
		double binomialPValue = binomialTest.binomialTest(sampleSize, successes, probSuccess, AlternativeHypothesis.TWO_SIDED);
		assertTrue(z < 0.1);
		assertTrue(gaussianPValue > 0.9);
		assertTrue(binomialPValue > 0.9);
	}

	protected static class Item {
		private final int originalIndex;
		private final int classification;

		Item(int originalIndex, int classification) {
			this.originalIndex = originalIndex;
			this.classification = classification;
		}

		int getOriginalIndex() {
			return originalIndex;
		}

		int getClassification() {
			return classification;
		}

		@Override
		public String toString() {
			return "Item{" +
					"originalIndex=" + originalIndex +
					", classification=" + classification +
					'}';
		}
	}
}
