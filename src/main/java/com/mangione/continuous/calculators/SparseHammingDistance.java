package com.mangione.continuous.calculators;

import java.util.Arrays;

import com.mangione.continuous.observations.ObservationInterface;

public class SparseHammingDistance<OBSERVATION extends ObservationInterface<Integer>> {

	public int calculateDistance(OBSERVATION obs1, OBSERVATION obs2) {
		int distance = 0;

		for (Integer columnIndex : obs1.getColumnIndexes()) {
			distance += obs1.getFeature(columnIndex).equals(obs2.getFeature(columnIndex)) ? 0 : 1;
		}

		for (Integer columnIndex : obs2.getColumnIndexes()) {
			distance += Arrays.binarySearch(obs1.getColumnIndexes(), columnIndex) < 0
					&& !obs1.getFeature(columnIndex).equals(obs2.getFeature(columnIndex)) ? 1 : 0;
		}
		return distance;
	}
}
