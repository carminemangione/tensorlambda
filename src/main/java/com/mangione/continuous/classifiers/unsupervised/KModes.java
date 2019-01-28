package com.mangione.continuous.classifiers.unsupervised;

import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;

public class KModes implements DistanceMeasurer<double[]>{


	private MersenneTwister random = new MersenneTwister();

	@Override
	public double distanceToCentroid(Cluster<? extends double[]> cluster, double[] observation) {
		double[] centroid = cluster.getCentroid();
		int dist = 0;
		for(int i = 0; i < observation.length; i++) {
			if(centroid[i] != observation[i])
				dist++;
		}
		return dist;
	}

	@Override
	public void updateCentroid(Cluster<double[]> cluster) {
		List<double[]> observations = cluster.getObservations();
		if(observations.isEmpty()) {
			cluster.setCentroid(null);
		}
		else {
			double[] modeOfDimensions = new double[cluster.getNumDimensions()];
			for(int i = 0; i < cluster.getNumDimensions(); i++) {
				int numFails = 0;
				int numSucc = 0;
				for(double[] obs : observations) {
					if(obs[i] == 1)
						numFails++;
					if(obs[i] == 0)
						numSucc++;
				}
				if(numFails == numSucc) {
					if(random.nextBoolean())
						modeOfDimensions[i] = 0;
					else
						modeOfDimensions[i] = 1;
				}
				else {
					modeOfDimensions[i] = numFails > numSucc ? 1 : 0;
				}
			}
			cluster.setCentroid(modeOfDimensions);
		}
	}
}
