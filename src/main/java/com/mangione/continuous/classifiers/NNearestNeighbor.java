package com.mangione.continuous.classifiers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import com.mangione.continuous.observationproviders.ArrayObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProviderInterface;
import com.mangione.continuous.observations.DiscreteExemplarFactory;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.zscale.ZScaleObservationProvider;


public class NNearestNeighbor {
    private final ZScaleObservationProvider<ExemplarInterface<Double, Integer>> cachedMemoryProvider;
    private final int n;

    @SuppressWarnings("WeakerAccess")
    public NNearestNeighbor(ObservationProviderInterface<Double, ExemplarInterface<Double, Integer>> provider, int n) throws Exception {
        this.n = n;
        cachedMemoryProvider = new ZScaleObservationProvider<>(
                new ArrayObservationProvider<>(provider, new DiscreteExemplarFactory()), new DiscreteExemplarFactory());
    }

    public int classify(ExemplarInterface<Double, Integer> observation) throws Exception {
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        SortedArray<DistanceAndLabel> lowestDistances = new SortedArray<>(n);

        cachedMemoryProvider.forEach(currentTrainObs -> {

            final ExemplarInterface<Double, Integer>  scaled = cachedMemoryProvider.scale(observation);
            final double distance = euclideanDistance.compute(
                    ArrayUtils.toPrimitive(scaled.getFeatures()),
                    ArrayUtils.toPrimitive(currentTrainObs.getFeatures()));
            DistanceAndLabel distanceAndLabel = new DistanceAndLabel(distance, currentTrainObs.getTarget());
            lowestDistances.add(distanceAndLabel);
        });

        Map<Integer, Integer> counts = new HashMap<>();
        lowestDistances.get().forEach(distance -> {
            counts.putIfAbsent(distance.getLabel(), 0);
            counts.put(distance.getLabel(), counts.get(distance.getLabel()) + 1);
        });
        final int[] currentMaxCount = {0};
        final int[] currentLabelOfMax = {0};
        counts.forEach((key, value) -> {
            if (value > currentMaxCount[0]) {
                currentMaxCount[0] = value;
                currentLabelOfMax[0] = key;
            }
        });
        return currentLabelOfMax[0];
    }

    private class DistanceAndLabel implements Comparable<DistanceAndLabel> {
        private final double distance;
        private final int label;

        private DistanceAndLabel(double distance, int label) {
            this.distance = distance;
            this.label = label;
        }

        int getLabel() {
            return label;
        }

        @Override
        public int compareTo(@Nonnull DistanceAndLabel o) {
            return Double.compare(distance, o.distance);
        }
    }
}
