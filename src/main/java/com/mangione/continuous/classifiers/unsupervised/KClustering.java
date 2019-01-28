package com.mangione.continuous.classifiers.unsupervised;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mangione.continuous.model.modelproviders.DataProvider;
import com.mangione.continuous.observations.dense.Observation;


public class KClustering<S extends Set<Integer>, T extends Observation> {

	private final List<Cluster<S>> clusters = new ArrayList<>();
	private final KMeansListener<T> listener;
	private final DataProvider<S> provider;
	private final DistanceMeasurer distanceMeasurer;

	public KClustering(){
		this.listener = null;
		this.provider = null;
		this.distanceMeasurer = null;
	}

	private KClustering(int numberOfClusters, DataProvider provider, KMeansListener<T> listener, DistanceMeasurer distanceMeasurer) {
		this.listener = listener;
		this.provider = provider;
		this.distanceMeasurer = distanceMeasurer;
		initializeClusters(numberOfClusters, clusters, provider);

		clusters.forEach(distanceMeasurer::updateCentroid);

		//if (listener != null)
		//	listener.reassignmentCompleted(clusters);

		addThePointsToTheInitialClusters(numberOfClusters, provider.getNumberOfLines());

		loopThroughJigglingOnCentroidsAndReCluster(1);
	}

	public KClustering(int numberOfClusters, DataProvider provider, DistanceMeasurer distanceMeasurer) {
		this(numberOfClusters, provider, null, distanceMeasurer);
	}

	public KClustering(int numberOfClusters, DataProvider provider, int numThreads, DistanceMeasurer distanceMeasurer) throws Exception {
		listener = null;
		this.distanceMeasurer = distanceMeasurer;
		ArrayList<Thread> threads = new ArrayList<>();
		this.provider = provider;


		initializeClusters(numberOfClusters, clusters, provider);

		clusters.forEach(distanceMeasurer::updateCentroid);

		int initial = numberOfClusters;
		for(int i = 0; i < numThreads; i++) {
			Thread thread = new Thread(new MultiThreadingHelper(initial, provider.getNumberOfLines() / numThreads));
			threads.add(thread);
			thread.start();
			initial += provider.getNumberOfLines() / numThreads;
		}
		for(Thread thread : threads) {
			thread.join();
		}

		loopThroughJigglingOnCentroidsAndReCluster(numThreads);

	}

//	public double getWithinClusterSumOfSquares() {
//		final double[] wcss = {0};
//		clusters.forEach(cluster-> wcss[0] += cluster.withinClusterSumOfSquares());
//		return wcss[0];
//	}

	public List<Cluster<S>> getClusters() {
		return clusters;
	}

	int getClusterIndex(double[] observation) {
		double distance = Double.MAX_VALUE;
		int index = 0;
		for (int j = 0; j < clusters.size(); j++) {
			if (distanceMeasurer.distanceToCentroid(clusters.get(j), observation) < distance) {
				distance = distanceMeasurer.distanceToCentroid(clusters.get(j), observation);
				index = j;
			}
		}
		return index;
	}

	private void loopThroughJigglingOnCentroidsAndReCluster(int numThreads) {
		boolean rejiggled;
		do {
			rejiggled = false;
			clusters.forEach(distanceMeasurer::updateCentroid);

			for (int i = 0; i < clusters.size(); i++) {
				rejiggled = processTheObservationsForThisCluster(i) || rejiggled;
			}

			//if (listener != null)
			//	listener.reassignmentCompleted(clusters);
			if(!rejiggled) {
				rejiggled = checkCorrectAnswer(clusters);
			}

		} while (rejiggled);

	}



	private boolean checkCorrectAnswer(List<Cluster<S>> clusters) {

		SparseKModes kModes = new SparseKModes();
		for (int i = 0; i < clusters.size(); i++) {
			for (S elem : clusters.get(i).getObservations()) {
				double dist = kModes.distanceToCentroid(clusters.get(i), elem);
				for (int j = 0; j < clusters.size(); j++) {
					if(kModes.distanceToCentroid(clusters.get(j), elem) < dist) {
						System.out.println(dist + "     " + kModes.distanceToCentroid(clusters.get(j), elem) +   "   " + j);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean processTheObservationsForThisCluster(int i) {
		boolean rejiggled = false;
		final Cluster<S> currentCluster = clusters.get(i);
		List<S> tempList = new ArrayList<>();
		List<S> observationsToMove = currentCluster.getObservations();
		for(int j = 0; j < observationsToMove.size(); j++) {
			S observation = observationsToMove.get(j);
			if(observation == null)
				continue;

			double currentDistance = distanceMeasurer.distanceToCentroid(currentCluster, observation);
			Cluster closest = findCloserClusterIfExists(i, observation, currentDistance);

			if(i == 3 && j == 0) {
				System.out.println("Current Distance: " + currentDistance);
				System.out.println("CLoseset : " + distanceMeasurer.distanceToCentroid(closest, observation));
			}

			rejiggled = moveObservationToCloserCluster(tempList, observation, closest) || rejiggled;
			//if (rejiggled && listener != null)
			//	listener.reassignmentCompleted(clusters);
		}
		tempList.forEach(currentCluster::remove);
		return rejiggled;
	}

	private boolean moveObservationToCloserCluster(List<S> currentCluster, S observation, Cluster closest) {
		boolean rejiggled = false;
		if (closest != null && closest.getObservations().size() > 1) {
			currentCluster.add(observation);
			closest.add(observation);
			rejiggled = true;
			//if (listener != null)
			//	listener.reassignmentCompleted(clusters);
		}
		return rejiggled;
	}

	private Cluster<S> findCloserClusterIfExists(int i, S observation, double currentDistance) {
		Cluster<S> closest = null;
		for (int j = 0; j < clusters.size(); j++) {
			if (i != j) {
				if (distanceMeasurer.distanceToCentroid(clusters.get(j), observation) < currentDistance) {
					closest = clusters.get(j);
				}
			}
		}
		return closest;
	}

	private String valueOfVec(double[] arr) {
		ArrayList<Integer> list = new ArrayList<>();
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] != 0) {
				list.add(i);
			}
		}
		return list.toString();
	}

	private void addThePointsToTheInitialClusters(int start, int length) {

		for(int i = start; i < start + length && i < provider.getNumberOfLines(); i++) {
			S nextObservation = provider.get(i);
			if(nextObservation == null)
				continue;
			Cluster closest = null;
			double distance = Double.MAX_VALUE;
			for (Cluster cluster : clusters) {
				final double toCentroid = distanceMeasurer.distanceToCentroid(cluster, nextObservation);
				if (toCentroid < distance) {
					closest = cluster;
					distance = toCentroid;
				}
			}
			//noinspection ConstantConditions

			closest.add(nextObservation);
		}
	}

	private void initializeClusters(int numberOfClusters, List<Cluster<S>> clusters, DataProvider<S> provider) {
		for (int i = 0; i < numberOfClusters; i++) {
			final S next = provider.get(i);
			final Cluster<S> cluster = new Cluster<>(provider.getLengthOfObservation());
			cluster.add(next);
			clusters.add(cluster);
		}
	}

	private class MultiThreadingHelper implements Runnable {

		private final int start;
		private final int length;

		MultiThreadingHelper(int start, int length) {
			this.start = start;
			this.length = length;
		}

		@Override
		public void run() {
			try {
				addThePointsToTheInitialClusters(start, length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
