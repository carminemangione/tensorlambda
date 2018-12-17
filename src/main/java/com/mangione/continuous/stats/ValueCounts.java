package com.mangione.continuous.stats;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;

public class ValueCounts {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValueCounts.class);
	private final List<Pair<Integer, Integer>> valueAndCounts;

	public ValueCounts(ObservationProviderInterface<Integer, ? extends ExemplarInterface<Integer, Integer>> provider, int column) {
		Map<Integer, Integer> valueToCount = new HashMap<>();
		int linesProcessed = 0;
		for (ExemplarInterface<Integer, Integer> next : provider) {
			Integer value = next.getAllColumns().get(column);
			int currentCount = valueToCount.computeIfAbsent(value, integer -> 0);
			valueToCount.put(value, ++currentCount);
			if (++linesProcessed % 100000 == 0) {
				break;
			}

			if (linesProcessed % 1000 == 0)
				LOGGER.info("Processed lines: " + linesProcessed);
		}
		valueAndCounts = valueToCount.entrySet()
				.stream()
		.map(entry -> new Pair<Integer, Integer>(entry.getKey(), entry.getValue()))
		.collect(Collectors.toList());

		valueAndCounts.sort(Comparator.comparing(Pair::getSecond));

	}

	public List<Pair<Integer, Integer>> getCounts() {
		return valueAndCounts;
	}
}
