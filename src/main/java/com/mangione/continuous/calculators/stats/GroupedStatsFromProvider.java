package com.mangione.continuous.calculators.stats;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.util.LoggingTimer;

public class GroupedStatsFromProvider {

	private final Map<Object, List<ColumnStats>> columnStats;
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupedStatsFromProvider.class);
	private final int groupByColumn;

	public GroupedStatsFromProvider(ObservationProviderInterface<Object, ObservationInterface<Object>> provider, int groupByColumn) {
		this.groupByColumn = groupByColumn;
		Map<Object, AllColumnsStats> nameToColumnStats = new HashMap<>();

		validateThatColumnsAreNumeric(provider, groupByColumn);
		LoggingTimer loggingTimer = new LoggingTimer(LOGGER, 1000, "Processed observations: ");
		int numberOfColumns = provider.getNumberOfColumns() - 1;
		for (ObservationInterface<Object> observation : provider) {
			try {
				AllColumnsStats allColumnsStats = nameToColumnStats.computeIfAbsent(observation.getFeature(groupByColumn),
						key -> new AllColumnsStats(numberOfColumns));
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

	private void validateThatColumnsAreNumeric(ObservationProviderInterface<Object, ObservationInterface<Object>> provider,
			int groupByColumn) {
		Iterator<ObservationInterface<Object>> iterator = provider.iterator();
		if (!iterator.hasNext())
			throw new IllegalStateException("Provider returned empty iterator");
		ObservationInterface<Object> next = iterator.next();
		Optional<Integer> nonNumbericValue = next.getColumnIndexes()
				.stream()
				.filter(index -> !index.equals(groupByColumn))
				.filter(index -> !(next.getFeature(index) instanceof Number))
				.findFirst();
		if (nonNumbericValue.isPresent())
			throw new IllegalStateException("Observation contains a non-numeric value: " + nonNumbericValue.get());
	}

	private List<Double> convertToDoubleRemovingGroupByColumn(ObservationInterface<Object> observation) {
		return IntStream.range(0, observation.getFeatures().size())
				.boxed()
				.filter(index -> index != groupByColumn)
				.map(index -> ((Number) observation.getFeature(index)).doubleValue())
				.collect(Collectors.toList());
	}
}