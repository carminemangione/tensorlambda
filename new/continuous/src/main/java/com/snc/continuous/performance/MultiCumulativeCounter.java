package com.mangione.continuous.performance;


import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

class MultiCumulativeCounter<TagType extends Comparable<TagType>> {
	private final TreeMap<TagType, CumulativeCounter> mapOfCounters;
	private final CumulativeCounter allCounter;

	private MultiCumulativeCounter(TreeMap<TagType, CumulativeCounter> mapOfCounters, CumulativeCounter allCounter) {
		this.mapOfCounters = mapOfCounters;
		this.allCounter = allCounter;
	}

	public CumulativeCounter getCounter(TagType tag) {
		return mapOfCounters.get(tag);
	}

	public CumulativeCounter getAllCounter() {
		return allCounter;
	}

	public TreeSet<TagType> getTags() {
		return new TreeSet<>(mapOfCounters.keySet());
	}

	public static class Builder<TagType extends Comparable<TagType>> {
		private final CumulativeCounter.Builder allBuilder = new CumulativeCounter.Builder();
		private final TreeMap<TagType, CumulativeCounter.Builder> counterBuilderMap = new TreeMap<>();

		public Builder<TagType> add(double score, TagType t) {
			counterBuilderMap.putIfAbsent(t, new CumulativeCounter.Builder());

			CumulativeCounter.Builder builder = counterBuilderMap.get(t);
			builder.add(score);
			allBuilder.add(score);
			return this;
		}

		public MultiCumulativeCounter<TagType> build() {
			 return new MultiCumulativeCounter<>(buildMapOfCounters(), allBuilder.build());
		}

		private TreeMap<TagType, CumulativeCounter> buildMapOfCounters() {
			return counterBuilderMap.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().build(), (o1, o2) -> o1, TreeMap::new));
		}
 	}
}
