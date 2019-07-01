package com.mangione.continuous.classifiers.unsupervised;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.DoubleStream;
import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;


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


	/*
	#############################
	kmeans++
	#############################
	 */


	/* takes 2 of same sized double arrays for subtraction */
	private double[] matSub(double[] X1, double[] X2) {
		double[] result = new double[X1.length];
		int i;
		for(i = 0; i < X1.length; i++) {
			result[i] = X1[i] - X2[i];
		}
		return result;
	}

	/* dot product: X1 T X2 */
	private double matDot(double[] X1, double[] X2) {
		double dotProd = 0;
		int i;
		for(i = 0; i < X1.length; i++) {
			dotProd += X1[i] * X2[i];
		}
		return dotProd;
	}

//	/* Calculates the D values */
//	private double[] Dx(int i, List<double[]> centroids,
//						ObservationProviderInterface<S, T> provider) {
//
//		int num_features = provider.iterator().next().numberOfFeatures();
//		double[] dFinals = new double[num_features];
//		int l = 0;
//
//		for(T observation: provider) {
//
//			List<Double> dChoices  = new ArrayList<Double>();
//
//			// over however many cluster centers we have thus far
//			for(int k = 0; k < i; k++) {
//
//				// the matrix subtraction
//				double [] xcDiff = matSub(toDoubleArray(observation), centroids.get(k));
//
//				// the matrix multiplication
//				double index = matDot(xcDiff, xcDiff);
//
//				dChoices.add(index);
//			}
//			double min_index = Collections.min(dChoices);
//			dFinals[l] = min_index;
//			l++;
//		}
//		return dFinals;
//	}

	/* assigns the probabilities as per the Dx values */
	private double[] assignProbs(double[] Dx) {
		double[] probs = new double[Dx.length];
		double sumOfDx = DoubleStream.of(Dx).sum();
		int i;
		for(i = 0; i < Dx.length; i++) {
			probs[i] = Dx[i] / sumOfDx;
		}
		return probs;
	}

	/* finds cumulative sum of entries */
	private double[] cumSum(double[] probs) {
		int pLen = probs.length;
		double[] cumProbs = new double[pLen];
		double accum = 0;
		int i;
		for (i = 0; i < pLen; i++) {
			accum += probs[i];
			cumProbs[i] = accum;
		}
		return cumProbs;
	}

//	private centroidDistribution(ArrayList<>) {
//
//	}
//	private void initializeClustersPlusPlus(int numberOfClusters, List<Cluster<S, T>> clusters,
//											ObservationProviderInterface<S, T> provider) {
//		int num_features = provider.iterator().next().numberOfFeatures();
//		int initCentroidIndex = ThreadLocalRandom.current().nextInt(0, num_features);
//
//
//
//
//	}


}
