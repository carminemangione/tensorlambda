package com.mangione.continuous.calculators;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.util.concurrent.AtomicDouble;

public class CumulativeDistributionFunction {

	private final List<Double> fCDF;

	public CumulativeDistributionFunction(List<Integer> counts) {
		counts.sort(Comparator.reverseOrder());
		double total = counts.stream()
				.mapToDouble(Integer::doubleValue)
				.sum();
		AtomicDouble currentCdf = new AtomicDouble(0);
		fCDF = counts.stream()
				.mapToDouble(Integer::doubleValue)
				.boxed()
				.mapToDouble(currentCdf::getAndAdd)
				.boxed()
				.mapToDouble(x -> x / total)
				.boxed()
				.collect(Collectors.toList());
	}



	public List<Double> getCDF() {
		return fCDF;
	}
}
