package com.mangione.continuous.classifiers.supervised.ranking.xgboost;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.util.coersion.CoerceToIntArray;

public class Groups<FEATURE extends Number, EXEMPLAR extends ExemplarInterface<FEATURE, Integer>> {
	private final int[] groups;
	private int currentGroup;

	public Groups(ObservationProviderInterface<FEATURE, EXEMPLAR> train, Function<EXEMPLAR, Integer> groupingFunction) {
		groups = calculateGroups(train, groupingFunction);
	}

	public int[] getGroups() {
		return groups;
	}

	private int[] calculateGroups(ObservationProviderInterface<FEATURE, EXEMPLAR> provider, Function<EXEMPLAR, Integer> groupFunction) {
		Iterator<EXEMPLAR> iterator = provider.iterator();
		List<Integer> groupCounts = new ArrayList<>();
		currentGroup = groupFunction.apply(iterator.next());
		int startGroup;
		do {
			startGroup = currentGroup;
			groupCounts.add(getNextGroupCount(iterator, groupFunction));
		} while (iterator.hasNext());

		if (startGroup != currentGroup)
			groupCounts.add(1);
		return CoerceToIntArray.coerce(groupCounts);
	}

	private int getNextGroupCount(Iterator<EXEMPLAR> iterator, Function<EXEMPLAR, Integer> groupFunction) {
		int nextGroup;
		int count = 1;
		do {
			nextGroup = groupFunction.apply(iterator.next());
			if (nextGroup == currentGroup)
				count++;
		} while (iterator.hasNext() && nextGroup == currentGroup);
		currentGroup = nextGroup;
		return count;
	}
}
