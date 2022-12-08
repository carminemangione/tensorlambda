package com.mangione.continuous.calculators;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import com.google.common.util.concurrent.AtomicDouble;

public class CumulativeDistributionFunctionTest {
	@Test
	public void calculatesForListOfIntegersNonSorted() {
		List<Integer> counts = IntStream.range(0, 10).boxed().collect(Collectors.toList());
		List<Double> cdf = new CumulativeDistributionFunction(counts).getCDF();

		int total = counts.stream().mapToInt(Integer::intValue).sum();
		assertEquals(0d, cdf.get(0), Double.MIN_VALUE);
		assertEquals(1d, cdf.get(9), Double.MIN_VALUE);
		AtomicDouble currentCdf = new AtomicDouble( 0d);
		IntStream.range(9, 0)
				.boxed()
				.mapToDouble(Integer::doubleValue)
				.forEach(x->assertEquals(currentCdf.addAndGet(x / total), cdf.get((int)x), Double.MIN_VALUE));

	}
}