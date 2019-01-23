package com.mangione.continuous.classifiers.unsupervised;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.random.MersenneTwister;

import com.google.common.collect.Sets;

public class SparseKModes implements DistanceMeasurer<Set<Integer>>{


	private MersenneTwister random = new MersenneTwister();

	@Override
	public double distanceToCentroid(Cluster<Set<Integer>> cluster, Set<Integer> observation) {
		Set<Integer> centroid = cluster.getCentroid();


		Set<Integer> tempList = new HashSet<>();

		tempList.addAll(centroid);
		tempList.addAll(observation);
		tempList.removeAll(Sets.intersection(centroid, observation));

		return tempList.size();
	}

	@Override
	public void updateCentroid(Cluster<Set<Integer>> cluster) {
		List<Set<Integer>> observations = cluster.getObservations();
		if(observations.isEmpty()) {
			cluster.setCentroid(null);
		}
		else {
			Set<Integer> modeOfDimensions = new HashSet<>();
			for(int i = 0; i < cluster.getNumDimensions(); i++) {
				int numFails = 0;
				int numSucc = 0;
				for(Set<Integer> obs : observations) {
					if(obs.contains(i))
						numFails++;
					else
						numSucc++;
				}
				if(numFails == numSucc) {
					if(random.nextBoolean())
						modeOfDimensions.add(i);
				}
				else {
					if(numFails > numSucc) {
						modeOfDimensions.add(i);
					}
				}
			}
			cluster.setCentroid(modeOfDimensions);
		}
	}
}
