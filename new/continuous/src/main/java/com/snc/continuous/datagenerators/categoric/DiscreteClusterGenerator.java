package com.mangione.continuous.datagenerators.categoric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import com.mangione.continuous.observations.sparse.SparseExemplar;

public class DiscreteClusterGenerator {

	private final int numPointsInCluster;
	private final int numClusters;
	private final int numDimensions;
	private final int clusterSpread;
	private final Random random;

	public DiscreteClusterGenerator(int numPointsInCluster, int numClusters, int numDimensions, int clusterSpread, Random random) {
		this.numPointsInCluster = numPointsInCluster;
		this.numClusters = numClusters;
		this.numDimensions = numDimensions;
		this.clusterSpread = clusterSpread;
		this.random = random;
	}

	public Integer[][] generateClusters() {
		Integer[][] clusterCentroids = new Integer[4][];

		int clusterLoc = numDimensions / numClusters;
		for (int i = 0; i < numClusters; i++) {
			clusterCentroids[i] = IntStream.range(0, numDimensions).boxed().map(val -> 0).toArray(Integer[]::new);
			for (int j = 0; j < clusterLoc; j++)
				clusterCentroids[i][i + j] = 1;
		}

		List<Integer[]> generatedPoints = new ArrayList<>();
		for (int k = 0; k < numClusters; k++) {
			for (int i = 0; i < numPointsInCluster; i++) {
				int currentDistance = random.nextInt(clusterSpread);
				Integer[] currentPoint = Arrays.stream(clusterCentroids[k]).toArray(Integer[]::new);
				for (int j = 0; j < currentDistance; j++) {
					int featureToJuggle = random.nextInt(numDimensions);
					currentPoint[featureToJuggle] = currentPoint[featureToJuggle] == 1 ? 0 : 1;
				}
				generatedPoints.add(currentPoint);
			}
		}
		Collections.shuffle(generatedPoints, random);
		return generatedPoints.toArray(new Integer[0][]);
	}

}
