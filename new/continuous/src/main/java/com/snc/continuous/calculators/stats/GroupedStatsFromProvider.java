package com.mangione.continuous.calculators.stats;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.calculators.KeyFactory;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.util.LoggingTimer;

public class GroupedStatsFromProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupedStatsFromProvider.class);

	private final Map<Object, List<ColumnStats>> columnStats;
	private final Set<Integer> numericColumnIndexes;

	public GroupedStatsFromProvider(ObservationProviderInterface<Object, ObservationInterface<Object>> provider,
			 KeyFactory<Object, ObservationInterface<Object>> keyFactory) {
		Map<Object, AllColumnsStats> nameToColumnStats = new HashMap<>();

		numericColumnIndexes = findNonNumericColumns(provider);
		LoggingTimer loggingTimer = new LoggingTimer(LOGGER, 1000, "Processed observations: ");
		int numberOfColumns = provider.getNumberOfFeatures() - 1;
		for (ObservationInterface<Object> observation : provider) {
			try {
				Object key = keyFactory.generateKey(observation);
				AllColumnsStats allColumnsStats = nameToColumnStats.computeIfAbsent(key,
						x -> new AllColumnsStats(numberOfColumns));
				allColumnsStats.add(convertToDoubleRemovingGroupByColumn(observation));
			} catch (Exception e) {
				LOGGER.error("Could not process stats for observation: " + observation, e);
				throw e;
			}
			loggingTimer.nextStep();
		}

		columnStats = nameToColumnStats
				.entrySet()
				.stream()
				.collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().getColumnStats()));
	}

	public Map<Object, List<ColumnStats>> getStats() {
		return columnStats;
	}

	private Set<Integer> findNonNumericColumns(ObservationProviderInterface<Object, ObservationInterface<Object>> provider) {
		Iterator<ObservationInterface<Object>> iterator = provider.iterator();
		if (!iterator.hasNext())
			throw new IllegalStateException("Provider returned empty iterator");
		ObservationInterface<Object> next = iterator.next();
		return Arrays
				.stream(next.getColumnIndexes())
				.boxed()
				.filter(index -> next.getFeature(index) instanceof Number)
				.collect(Collectors.toSet());
	}

	private List<Double> convertToDoubleRemovingGroupByColumn(ObservationInterface<Object> observation) {
		return IntStream.range(0, observation.getFeatures(Integer[]::new).length)
				.boxed()
				.filter(numericColumnIndexes::contains)
				.map(index -> ((Number) observation.getFeature(index)).doubleValue())
				.collect(Collectors.toList());
	}
}