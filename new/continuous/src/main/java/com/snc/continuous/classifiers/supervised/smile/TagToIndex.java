package com.mangione.continuous.classifiers.supervised.smile;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TagToIndex {
	private final Map<Integer, Integer> tagToIndex;
	private final Map<Integer, Integer> indexToTag;

	public TagToIndex(int[] labels) {
			AtomicInteger i = new AtomicInteger();
		tagToIndex = Arrays.stream(labels)
					.distinct()
					.boxed()
					.collect(Collectors.toList())
					.stream()
					.sorted()
					.collect(Collectors.toMap(Function.identity(), x -> i.getAndIncrement(), (o1,o2)->o1, TreeMap::new));

		indexToTag = tagToIndex.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

	}

	public Integer getIndex(int tag) {
		return tagToIndex.get(tag);
	}

	public int size() {
		return tagToIndex.size();
	}

	public Integer getTag(int index){
		return indexToTag.get(index);
	}

	public Set<Integer> getTags() {
		return tagToIndex.keySet();
	}
}
