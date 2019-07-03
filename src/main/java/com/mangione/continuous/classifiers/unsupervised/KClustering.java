package com.mangione.continuous.classifiers.unsupervised;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.DoubleStream;
import javax.annotation.Nonnull;
import javax.xml.crypto.Data;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;


import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.ObservationInterface;
import com.mangione.continuous.model.modelproviders.DataProvider;
import com.mangione.continuous.observations.dense.Observation;
import org.jetbrains.annotations.NotNull;


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

//    double[] fork = {1.0, 2.0, 3.0};
//    ObservationInterface<T> observation = new Observation(Arrays.asList(fork));

    private double[] Dx(int i, List<T> centroids,
                        ObservationProviderInterface<S, T> provider) {

        int num_features = provider.iterator().next().numberOfFeatures();
        double[] dFinals = new double[num_features];
        int l = 0;

        for(T observation: provider) {

            List<Double> dChoices  = new ArrayList<Double>();

            // over however many cluster centers we have thus far
            for(int k = 0; k < i; k++) {
                T curCentroid = centroids.get(k);

                // the matrix subtraction
                double[] xcDiff = IntStream.range(0, num_features)
                        .mapToDouble(id -> observation.getFeature(id).doubleValue()
                                - curCentroid.getFeature(id).doubleValue()).toArray();


                // the matrix multiplication
                DoubleStream indices = DoubleStream.of(xcDiff);
                double index = indices.map(elt -> elt * elt).sum();

                dChoices.add(index);
            }
            double minD = Collections.min(dChoices);
            dFinals[l] = minD;
            l++;
        }
        return dFinals;
    }

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

    private int centroidDistribution(double[] cumProbs) {
        double d = ThreadLocalRandom.current().nextDouble();
        int i, index = 0;
        for(i = 0; i < cumProbs.length; i++) {
            if(d <= cumProbs[i]) {
                index = i;
                break;
            }
        }
        return index;
    }

    // runs through the provider to select the needed point
    // ask about default
    private T providerRunThru(ObservationProviderInterface<S, T> provider, int index) {
        Iterator<T> iter = provider.iterator();
        T obs = iter.next();
        iter = provider.iterator();
        int i = 0;
        while (iter.hasNext() && i <= index) {
            obs = iter.next();
            i++;
        }
        return obs;
    }

    private void initializeClustersPlusPlus(int numberOfClusters, List<Cluster<S, T>> clusters,
                                            ObservationProviderInterface <S, T> provider) {
        int num_features = provider.iterator().next().numberOfFeatures();
        int initCentroidIndex = ThreadLocalRandom.current().nextInt(0, num_features);
        ArrayList<T> centroids = new ArrayList<>();
        T initCentroid = providerRunThru(provider, initCentroidIndex);
        centroids.add(initCentroid);

        int i;
        for(i = 1; i < clusters.size(); i++) {
            double[] dxVals = Dx(i, centroids, provider);
            double[] probs = assignProbs(dxVals);
            double[] cumProbs = cumSum(probs);
            int centroidIndex = centroidDistribution(cumProbs);
            T nextCentroid = providerRunThru(provider, centroidIndex);
            centroids.add(nextCentroid);
        }

        int j;
        for(j = 0; j < numberOfClusters; j++) {
            Cluster<S, T> cluster = new Cluster<>(num_features, distanceMeasureInterface);
            cluster.add(centroids.get(j));
            clusters.add(cluster);
        }

        addThePointsToTheInitialClusters();

    }


}

