package com.mangione.continuous.classifiers.unsupervised;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;


public class KClustering<S extends Number, T extends ObservationInterface<S>> {

	private final List<Cluster<S, T>> clusters = new ArrayList<>();
	private final ObservationProviderInterface<S, T> provider;
	private final DistanceMeasureInterface<S, T> distanceMeasureInterface;

	public KClustering(int numberOfClusters,  ObservationProviderInterface<S, T> provider,
			DistanceMeasureInterface<S, T> distanceMeasureInterface)  {
		this.distanceMeasureInterface = distanceMeasureInterface;
		this.provider = provider;

		initializeClusters(numberOfClusters, clusters, provider);
		loopThroughJigglingOnCentroidsAndReCluster();
	}

	public List<Cluster<S, T>> getClusters() {
		return clusters;
	}

	private void loopThroughJigglingOnCentroidsAndReCluster() {
		boolean rejiggled;
		do {
			rejiggled = false;
			clusters.forEach(Cluster::updateCentroid);

			for (Cluster <S, T> cluster : clusters) {
				rejiggled = processTheObservationsForThisCluster(cluster) || rejiggled;
			}

		} while (rejiggled);
	}

	private boolean processTheObservationsForThisCluster(Cluster<S, T> currentCluster) {
		boolean rejiggled = false;
		Iterator<T> iterator = currentCluster.getObservations().iterator();
		while (iterator.hasNext()) {
			T next = iterator.next();
			Cluster<S, T> closestCluster = getClosestCluster(next);
			if (closestCluster != currentCluster) {
				iterator.remove();
				closestCluster.add(next);
				rejiggled = true;
			}
		}
		return rejiggled;
	}

	private void addThePointsToTheInitialClusters() {
		for (T observation : provider) {
			Cluster<S, T> closest = getClosestCluster(observation);
			closest.add(observation);
		}
	}

	@Nonnull
	private Cluster<S, T> getClosestCluster(T observation) {
		Cluster<S, T> closest = null;
		double distance = Double.MAX_VALUE;
		for (Cluster<S, T> cluster : clusters) {
			double toCentroid = cluster.distanceToCentroid(observation);
			if (toCentroid < distance) {
				closest = cluster;
				distance = toCentroid;
			}
		}
		//noinspection ConstantConditions
		return closest;
	}

	private void initializeClusters(int numberOfClusters, List<Cluster<S, T>> clusters,
			ObservationProviderInterface<S, T> provider) {
		Iterator<T> iterator = provider.iterator();
		int clusterIndex = 0;
		while (iterator.hasNext() && clusterIndex++ < numberOfClusters) {
			T next = iterator.next();
			Cluster<S, T> cluster = new Cluster<>(next.numberOfFeatures(), distanceMeasureInterface);
			cluster.add(next);
			clusters.add(cluster);
		}
		addThePointsToTheInitialClusters();
	}

}
