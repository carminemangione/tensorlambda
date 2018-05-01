package com.mangione.continuous.datagenerators;

import com.mangione.continuous.observations.DiscreteExemplar;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.ArrayList;
import java.util.List;

public class ScaledDiscreteExemplarGenerator {


    private final DiscreteExemplarGenerator discreteExemplarGenerator;
    private final double[][] featureMeanAndSD;

    ScaledDiscreteExemplarGenerator(int numberOfExemplars, double[][] featureMeanAndSD, double bias, double sdEpsilon, MersenneTwister twister) {
        this.featureMeanAndSD = featureMeanAndSD;
        discreteExemplarGenerator = new com.mangione.continuous.datagenerators.DiscreteExemplarGenerator(featureMeanAndSD.length, numberOfExemplars, bias, sdEpsilon, twister);

    }

    public List<DiscreteExemplar<Double>> generateDataSet() {
        final List<DiscreteExemplar<Double>> exemplars = discreteExemplarGenerator.getExemplars();
        final List<DiscreteExemplar> scaledExemplars = new ArrayList<>();
        exemplars.forEach(exemplar -> {
            for (int i = 0; i < exemplar.getFeatures().length; i++) {
                exemplar.getFeatures()[i] = exemplar.getFeatures()[i] * featureMeanAndSD[i][1] + featureMeanAndSD[i][0];
                scaledExemplars.add(new DiscreteExemplar(exemplar.getFeatures(), exemplar.getContinuousValue(), exemplar.getTarget()));
            }
        });
        return exemplars;
    }
}
