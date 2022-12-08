package com.mangione.continuous.performance;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CumulativeCounter {
	public int getTotalCount() {
		return totalCount;
	}

	private int totalCount;
	private TreeMap<Double, Integer> cumulativeCounts;

	private CumulativeCounter(int totalCount, TreeMap<Double, Integer> cumulativeCounts) {
		this.totalCount = totalCount;
		this.cumulativeCounts = cumulativeCounts;
	}

	public Set<Double> getScores() {
		return cumulativeCounts.keySet();
	}

	public int countLE(Double key) {
		int count;
		if (cumulativeCounts.isEmpty() || key < cumulativeCounts.firstKey())
			count = 0;
		else if (cumulativeCounts.containsKey(key))
			count = cumulativeCounts.get(key);
		else {
			Double glbKey = greatestLowerBound(key);
			count = cumulativeCounts.get(glbKey);
		}
		return count;
	}

	public int countGT(Double key) {
		return totalCount - countLE(key);
	}

	public int get(Double k) {
		if (cumulativeCounts.isEmpty())
			return 0;
		return cumulativeCounts.get(k);
	}

	@Override
	public String toString() {
		String mapAsString = cumulativeCounts.keySet().stream()
				.map(key -> key + "=" + cumulativeCounts.get(key))
				.collect(Collectors.joining(", ", "{", "}"));
		return "CumulativeCounter{" +
				"totalCount=" + totalCount +
				", cumulativeCounts=" + mapAsString +
				'}';
	}

	public static class Builder {
		int totalCount = 0;
		TreeMap<Double, Integer> counts = new TreeMap<>();

		public Builder() {
		}

		public Builder add(Double d) {
			if (d < 0)
				throw new IllegalArgumentException(String.format("Value %f is less than zero.", d));
			if (!counts.containsKey(d)) {
				counts.put(d, 0);
			}
			counts.put(d, counts.get(d) + 1);
			totalCount++;
			return this;
		}

		public CumulativeCounter build() {
			int sum = 0;
			for (Double d : counts.keySet()) {
				sum += counts.get(d);
				counts.put(d, sum);
			}
			return new CumulativeCounter(totalCount, counts);
		}
	}

	private Double greatestLowerBound(Double key) {
		Double lowerBound;
		Map.Entry<Double, Integer> e = cumulativeCounts.lowerEntry(key);
		if (e == null)
			lowerBound = 0.;
		else
			lowerBound = e.getKey();
		return lowerBound;
	}

}
