package com.mangione.continuous.encodings.hashing;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import org.ejml.data.DMatrixD1;
import org.ejml.dense.row.CommonOps_DDRM;
javax.annotation.Nonnull;
import org.junit.Assert;
import org.junit.Test;

import com.mangione.continuous.encodings.random.WRSampler;
import com.mangione.continuous.linalg.vector.VectorMapRepresentation;

public class FeatureHashTest {

	@Test
	public void testHash1() {
		int numberOfChoices = 17;
		int numberOfCellsInDomain = 32;
		int numberOfCellsInRange = 5;
		Random random = new Random(1305385);
		WRSampler wrSampler = new WRSampler(numberOfChoices, numberOfCellsInDomain, random);
		FeatureHash featureHash = new FeatureHash(numberOfCellsInDomain, numberOfCellsInRange, 305, 385);
		int sampleCount = 4;
		int[][] samples = new int[sampleCount][];
		for (int r = 0; r < samples.length; r++) {
			int[] featureSet = wrSampler.next();
			samples[r] = featureHash.hash(featureSet);
		}
		//No two hashed Feature Sets Are Equal.
		for (int i = 0; i < sampleCount; i++) {
			for (int j = i + 1; j < sampleCount; j++) {
				Assert.assertFalse(Arrays.equals(samples[i], samples[j]));
			}
		}
	}


	@Test
	public void testRandomInnerProduct() {
		int trials = 10000;
		int numberOfChoices = 15;
		int numberOfCellsInDomain = 32;
		int numberOfCellsInRange = 5;
		Random random = new Random(1305385);
		WRSampler wrSampler = new WRSampler(numberOfChoices, numberOfCellsInDomain, random);
		VectorMapRepresentation input1, input2;
		input1 = populateVectorMap(numberOfCellsInDomain, wrSampler);
		input2 = populateVectorMap(numberOfCellsInDomain, wrSampler);
		double expectedDot = input1.dot(input2);
		double actualDot = 0.0;
		for (int t = 0; t < trials; t++) {
			FeatureHash featureHash = new FeatureHash(numberOfCellsInDomain, numberOfCellsInRange,
					random.nextInt(), random.nextInt());
			DMatrixD1 output1 = featureHash.apply(input1);
			DMatrixD1 output2 = featureHash.apply(input2);
			double sum = IntStream.range(0, output1.numCols).mapToDouble((i) -> output1.get(i) * output2.get(i)).sum();
			actualDot += sum;
		}
		actualDot /= trials;
		Assert.assertEquals(expectedDot, actualDot, 1.0e-2);
	}

	@Test
	public void testInnerProduct() {
		int trials = 1000000;
		int numberOfCellsInDomain = 8;
		int numberOfCellsInRange = 3;
		VectorMapRepresentation input1, input2;
		Random random = new Random(1305385);
		for (int r = 0; r < numberOfCellsInDomain; r++) {
			for (int c = r; c < numberOfCellsInDomain; c++) {
				input1 = VectorMapRepresentation.stdBasis(r, numberOfCellsInDomain);
				input2 = VectorMapRepresentation.stdBasis(c, numberOfCellsInDomain);
				double expectedDot = input1.dot(input2);
				double actualDot = 0.0;
				for (int t = 0; t < trials; t++) {
					FeatureHash featureHash = new FeatureHash(numberOfCellsInDomain, numberOfCellsInRange,
							random.nextInt(), random.nextInt());
					DMatrixD1 output1 = featureHash.apply(input1);
					DMatrixD1 output2 = featureHash.apply(input2);
					double delta = CommonOps_DDRM.dot(output1, output2);
					actualDot += delta;
				}
				Assert.assertEquals(expectedDot, actualDot / trials, 10.0 / Math.sqrt(trials));
			}
		}
	}

	@NotNull
	private VectorMapRepresentation populateVectorMap(int numberOfCellsInDomain, WRSampler wrSampler) {
		VectorMapRepresentation input1;
		input1 = new VectorMapRepresentation(numberOfCellsInDomain);
		for (int i : wrSampler.next()) {
			input1.set(i, 1.0);
		}
		return input1;
	}
}