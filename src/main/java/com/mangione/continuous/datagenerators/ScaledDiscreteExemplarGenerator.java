package com.mangione.continuous.datagenerators;

import com.mangione.continuous.observations.dense.DiscreteExemplar;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScaledDiscreteExemplarGenerator {


    private final DiscreteExemplarGenerator discreteExemplarGenerator;
    private final double[][] featureMeanAndSD;

    ScaledDiscreteExemplarGenerator(int numberOfExemplars, double[][] featureMeanAndSD, double bias, double sdEpsilon, MersenneTwister twister) {
        this.featureMeanAndSD = featureMeanAndSD;
        discreteExemplarGenerator = new com.mangione.continuous.datagenerators.DiscreteExemplarGenerator(featureMeanAndSD.length, numberOfExemplars, bias, sdEpsilon, twister);

    }

    public List<DiscreteExemplar<Double>> generateDataSet() {
        final List<DiscreteExemplar<Double>> exemplars = discreteExemplarGenerator.getExemplars();
        final List<DiscreteExemplar<Double>> scaledExemplars = new ArrayList<>();
        exemplars.forEach(exemplar -> {

            final int[] scaledIndex = {0};
            final List<Double> scaled = exemplar.getFeatures()
                    .stream()
                    .map(x -> x * featureMeanAndSD[scaledIndex[0]][1] + featureMeanAndSD[scaledIndex[0]++][0])
                    .collect(Collectors.toList());

            scaledExemplars.add(new DiscreteExemplar<>(scaled, exemplar.getContinuousValue(), exemplar.getTarget()));
        });
        return scaledExemplars;
    }
}
