package com.mangione.continuous.datagenerators;

import com.mangione.continuous.observations.dense.DiscreteExemplar;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.random.RandomGenerator;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiscreteExemplarGenerator{
    private final int numberOfDimensions;
    private final int numberExemplars;
    private final double bias;
    private final double sdEpsilon;
    private final RandomGenerator random;

    private int numberMiscategorized = 0;
    private final List<DiscreteExemplar<Double>> exemplars;

    private double[][] observationVector;

    DiscreteExemplarGenerator(int numberOfDimensions, int numberExemplars, double bias, double sdEpsilon, RandomGenerator random) {

        this.numberOfDimensions = numberOfDimensions;
        this.numberExemplars = numberExemplars;
        this.bias = bias;
        this.sdEpsilon = sdEpsilon;
        this.random = random;
        exemplars = generateDataSet();
    }

    public int getNumberMiscategorized() {
        return numberMiscategorized;
    }

    public List<DiscreteExemplar<Double>> getExemplars() {
        return exemplars;
    }

    private List<DiscreteExemplar<Double>> generateDataSet() {

        SimpleMatrix muMatrix = generateMuMatrix();
        SimpleMatrix designMatrix = generateDesignMatrix();
        SimpleMatrix epsilonMatrix = generateEpsilonMatrix();


        SimpleMatrix unjiggled = designMatrix.mult(muMatrix);
        SimpleMatrix jiggled = unjiggled.plus(epsilonMatrix);

        List<DiscreteExemplar<Double>> exemplars = new ArrayList<>();

        for (int i = 0; i < numberExemplars; i++) {
            final double jiggledY = jiggled.get(i, 0);
            final boolean didntFlip = (unjiggled.get(i, 0) < 0 && jiggledY < 0)
                    || (unjiggled.get(i, 0) > 0 && jiggledY > 0);
            numberMiscategorized += didntFlip ? 0 : 1;
            
            exemplars.add(new DiscreteExemplar<>(Arrays.asList(ArrayUtils.toObject(observationVector[i])), jiggledY, jiggledY > 0 ? 1 : 0));
        }
        return Collections.unmodifiableList(exemplars);
    }

    private SimpleMatrix generateEpsilonMatrix() {
        double[] epsilonVector = new RandomGaussianVector(numberExemplars, 0, sdEpsilon, false, random).nextVector();
        return createColumnMatrix(epsilonVector);
    }

    private SimpleMatrix generateMuMatrix() {
        double[] gaussianVector = new RandomUnitVector(numberOfDimensions, random).nextVector();
        double[] muMatrix = new double[numberOfDimensions + 1];
        muMatrix[0] = bias;
        System.arraycopy(gaussianVector, 0, muMatrix, 1, numberOfDimensions);
        return new SimpleMatrix(createColumnMatrix(muMatrix));
    }

    private SimpleMatrix createColumnMatrix(double[] vector) {
        double[][] matrix = new double[vector.length][1];
        for (int i = 0; i < vector.length; i++) {
            matrix[i][0] = vector[i];
        }
        return new SimpleMatrix(matrix);
    }

    private SimpleMatrix generateDesignMatrix() {
        double[][] designVector = new double[numberExemplars][];
        observationVector = new double[numberExemplars][numberOfDimensions];
        for (int i = 0; i < numberExemplars; i++) {
            designVector[i] = new RandomGaussianVector(numberOfDimensions, 0, 1, true, random).nextVector();
            System.arraycopy(designVector[i], 1, observationVector[i], 0, numberOfDimensions);
        }
        return new SimpleMatrix(designVector);
    }
}
