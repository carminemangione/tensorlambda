package com.mangione.continuous.classifiers.unsupervised;

import java.util.ArrayList;
import java.util.List;

import com.mangione.continuous.model.modelproviders.DoubleUnsupervisedModelProvider;
import com.mangione.continuous.observations.Observation;


public class KMeans<T extends Observation> {

	private final List<Cluster> clusters = new ArrayList<>();
	private final KMeansListener<T> listener;
	private final DoubleUnsupervisedModelProvider provider;

	public KMeans(int numberOfClusters, DoubleUnsupervisedModelProvider provider, KMeansListener<T> listener) throws Exception {
		this.listener = listener;
		this.provider = provider;
		initializeClusters(numberOfClusters);


		clusters.forEach(Cluster::updateCentroid);

		if (listener != null)
			listener.reassignmentCompleted(clusters);

		addThePointsToTheInitialClusters(numberOfClusters, provider.getNumberOfLines());

		loopThroughJigglingOnCentroidsAndReCluster(1);
	}

	public KMeans(int numberOfClusters, DoubleUnsupervisedModelProvider provider) throws Exception {
		this(numberOfClusters, provider, null);
	}

	public KMeans(int numberOfClusters, DoubleUnsupervisedModelProvider provider, int numThreads) throws Exception {
		listener = null;
		ArrayList<Thread> threads = new ArrayList<>();
		this.provider = provider;


		initializeClusters(numberOfClusters);

		clusters.forEach(Cluster::updateCentroid);

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

	public double getWithinClusterSumOfSquares() {
		final double[] wcss = {0};
		clusters.forEach(cluster-> wcss[0] += cluster.withinClusterSumOfSquares());
		return wcss[0];
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	public int getClusterIndex(double[] observation) {
		double distance = Double.MAX_VALUE;
		int index = 0;
		for (int j = 0; j < clusters.size(); j++) {
			if (clusters.get(j).distanceToCentroid(observation) < distance) {
				distance = clusters.get(j).distanceToCentroid(observation);
				index = j;
			}
		}
		return index;
	}

	private void loopThroughJigglingOnCentroidsAndReCluster(int numThreads) throws InterruptedException {
		boolean rejiggled = false;
		do {
			rejiggled = false;
			clusters.forEach(Cluster::updateCentroid);

			for (int i = 0; i < clusters.size(); i++) {
				rejiggled = processTheObservationsForThisCluster(i) || rejiggled;
			}

			if (listener != null)
				listener.reassignmentCompleted(clusters);

		} while (rejiggled);

	}

	private boolean processTheObservationsForThisCluster(int i) {
		boolean rejiggled = false;
		final Cluster currentCluster = clusters.get(i);
		List<double[]> observationsToMove = currentCluster.getObservations();
		for (double[] observation : observationsToMove) {
			if(observation == null)
				continue;
			double currentDistance = currentCluster.distanceToCentroid(observation);
			Cluster closest = findCloserClusterIfExists(i, observation, currentDistance);
			rejiggled = moveObservationToCloserCluster(currentCluster, observation, closest) || rejiggled;
			if (rejiggled && listener != null)
				listener.reassignmentCompleted(clusters);
		}
		return rejiggled;
	}

	private boolean moveObservationToCloserCluster(Cluster currentCluster, double[] observation, Cluster closest) {
		boolean rejiggled = false;
		if (closest != null) {
			currentCluster.remove(observation);
			closest.add(observation);
			rejiggled = true;
			if (listener != null)
				listener.reassignmentCompleted(clusters);
		}
		return rejiggled;
	}

	private Cluster findCloserClusterIfExists(int i, double[] observation, double currentDistance) {
		Cluster closest = null;
		for (int j = 0; j < clusters.size(); j++) {
			if (i != j) {
				if (clusters.get(j).getObservations().size() > 1 && clusters.get(j).distanceToCentroid(observation) < currentDistance) {
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

	private void addThePointsToTheInitialClusters(int start, int length) throws Exception {

		for(int i = start; i < start + length && i < provider.getNumberOfLines(); i++) {
			double[] nextObservation = provider.get(i);
			if(nextObservation == null)
				continue;
			Cluster closest = null;
			double distance = Double.MAX_VALUE;
			for (Cluster cluster : clusters) {
				final double toCentroid = cluster.distanceToCentroid(nextObservation);
				if (toCentroid < distance) {
					closest = cluster;
					distance = toCentroid;
				}
			}
			//noinspection ConstantConditions

			closest.add(nextObservation);
		}
	}

	private void initializeClusters(int numberOfClusters) throws Exception {
		for (int i = 0; i < numberOfClusters; i++) {
			final double[] next = provider.get(i);
			final Cluster cluster = new Cluster(next.length);
			cluster.add(next);
			clusters.add(cluster);
		}
	}

	private class MultiThreadingHelper implements Runnable {

		private final int start;
		private final int length;

		public MultiThreadingHelper(int start, int length) {
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
