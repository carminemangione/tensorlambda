package com.mangione.continuous.performance.confusionmatrix;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

import com.mangione.continuous.performance.ROCInterface;
import com.mangione.continuous.performance.ROCPointInterface;
import com.mangione.continuous.performance.RocBuilderInterface;

public class BinaryROCConfusionMatrixTest {
	@Test
	public void twoMatrices() {
		BinaryROCConfusionMatrix.Builder builder = new BinaryROCConfusionMatrix.Builder(2);
		generatePoints(10, .25, 1, 1, builder);
		generatePoints(20, .25, 0, 1, builder);
		generatePoints(30, .25, 0, 0, builder);
		generatePoints(40, .25, 1, 0, builder);

		generatePoints(50, .5, 1, 1, builder);
		generatePoints(20, .5, 0, 1, builder);
		generatePoints(30, .5, 0, 0, builder);
		generatePoints(40, .5, 1, 0, builder);

		ROCInterface<ROCConfusionMatrixPoint> build = builder.build();
		List<ROCConfusionMatrixPoint> roc = build.getROC();
		assertEquals(2, roc.size());
		ROCPointInterface rocPoint = roc.get(0);
		assertEquals(50d/90, rocPoint.getTruePositiveRate(), Double.MIN_VALUE);
		assertEquals(0.6, rocPoint.getTrueNegativeRate(), Double.MIN_VALUE);
		assertEquals(0.4, rocPoint.getFalsePositiveRate(), Double.MIN_VALUE);
		assertEquals(1 - 50d/90, rocPoint.getFalseNegativeRate(), Double.MIN_VALUE);
		assertEquals(.5, rocPoint.getThreshold(), Double.MIN_VALUE);

		rocPoint = roc.get(1);
		assertEquals(.2, rocPoint.getTruePositiveRate(), Double.MIN_VALUE);
		assertEquals(0.6, rocPoint.getTrueNegativeRate(), Double.MIN_VALUE);
		assertEquals(0.4, rocPoint.getFalsePositiveRate(), Double.MIN_VALUE);
		assertEquals(0.8, rocPoint.getFalseNegativeRate(), Double.MIN_VALUE);
		assertEquals(.25, rocPoint.getThreshold(), Double.MIN_VALUE);
	}

	private void generatePoints(int num, double threshold, int actual, int predicted,
			RocBuilderInterface<ROCConfusionMatrixPoint> builder) {
		for (int i = 0; i < num; i++) {
			builder.add(threshold, actual, predicted);
		}
	}
}