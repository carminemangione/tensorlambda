package com.mangione.assignments.assignment2;

import com.mangione.continuous.classifiers.NNearestNeighbor;
import com.mangione.continuous.observationproviders.*;
import com.mangione.continuous.observations.*;
import org.apache.commons.math3.random.MersenneTwister;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Assignment2 {

    public static void main(String[] args) throws Exception {
        String[] files = {"Seperable.csv", "3percent-miscategorization.csv", "10percent-miscatergorization.csv"};

        for (String currentFile : files) {
            processSingleFile(currentFile);
        }
        processAbalone();
    }


    private static void processSingleFile(String currentFile) throws Exception {
        File file = new File(Assignment2.class.getClassLoader()
                .getResource("com/mangione/continuous/assignment2/" + currentFile).toURI());


        CsvObservationProvider<Double, ExemplarInterface<Double, Integer>> observationProvider = new CsvObservationProvider<>(file,
                new DiscreteExemplarFactory(), new DoubleVariableCalculator(), new DoubleArraySupplier());

        ObservationProvider<Double, ExemplarInterface<Double, Integer>> trainingSetProvider =
                new SampledObservationProvider<>(
                        .10, observationProvider,
                        new DiscreteExemplarFactory(),
                        new MersenneTwister(2123), false);


        int[] numNeighbors = {6, 12, 24, 36, 72};

        for (int numNeighbor : numNeighbors) {
            observationProvider.reset();

            int[][] confusionMatrix = new int[2][2];
            NNearestNeighbor<ExemplarInterface<Double, Integer>> nearestNeighbor =
                    new NNearestNeighbor<>(trainingSetProvider, new DiscreteExemplarFactory(), numNeighbor);

            observationProvider.reset();
            ObservationProvider<Double, ExemplarInterface<Double, Integer>> testSetProvider =
                    new SampledObservationProvider<>(.10, observationProvider,
                            new DiscreteExemplarFactory(), new MersenneTwister(2123), false);
            testAndScore(confusionMatrix, nearestNeighbor, testSetProvider);

            System.out.println("file = " + currentFile);
            System.out.println("n = " + numNeighbor);
            System.out.println(String.format("%d , %d " ,confusionMatrix[1][1], confusionMatrix[1][0]));
            System.out.println(String.format("%d , %d " ,confusionMatrix[0][1], confusionMatrix[1][1]));
            double numCorrect =  (double)confusionMatrix[1][1] + confusionMatrix[0][0];
            double numWrong =  (double)confusionMatrix[1][0] + confusionMatrix[0][1];
            System.out.println("Accuracy = " + numCorrect / (numCorrect + numWrong));
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    private static void processAbalone() throws Exception {
        File file = new File(Assignment2.class.getClassLoader()
                .getResource("com/mangione/continuous/abalone/" + "abalone.data").toURI());

        Map<Integer, VariableCalculator> calculators = new HashMap<>();
        calculators.put(0, feature -> {
            double[] out = new double[3];
            switch (feature) {
                case "M":
                    out[0] = 1;
                    break;
                case "F":
                    out[1] = 1;
                    break;
                case "I":
                    out[2] = 1;
                    break;
            }
            return Arrays.asList(out);
        });

        CsvObservationProvider<Double, ExemplarInterface<Double, Integer>> observationProvider =
                new CsvObservationProvider<>(file, new DiscreteExemplarFactory(), new DoubleVariableCalculator(),
                        new DoubleArraySupplier());

        ObservationProvider<Double, ExemplarInterface<Double, Integer>> trainingSetProvider =
                new SampledObservationProvider<>(.10, observationProvider,
                        new DiscreteExemplarFactory(), new MersenneTwister(2123), false);

        int[] numNeighbors = {6, 12, 24, 36, 72};

        for (int numNeighbor : numNeighbors) {
            observationProvider.reset();

            int[][] confusionMatrix = new int[30][30];
            NNearestNeighbor<ExemplarInterface<Double, Integer>> nearestNeighbor =
                    new NNearestNeighbor<>(trainingSetProvider, new DiscreteExemplarFactory(),
                            numNeighbor);

            observationProvider.reset();
            ObservationProvider<Double, ExemplarInterface<Double, Integer>> testSetProvider =
                    new SampledObservationProvider<>(.10,
                            observationProvider, new DiscreteExemplarFactory(), new MersenneTwister(2123), false);

            testAndScore(confusionMatrix, nearestNeighbor, testSetProvider);

            System.out.println("file = abalone.csv");
            System.out.println("n = " + numNeighbor);
            double numCorrect = 0;
            double numWrong = 0;
            for (int i = 0; i < confusionMatrix.length; i++) {
                for (int j = 0; j < confusionMatrix[i].length; j++) {
                    System.out.print(String.format("%d ," , confusionMatrix[i][j]));
                    numCorrect +=  i == j ? confusionMatrix[i][j] : 0;
                    numWrong +=  i != j ? confusionMatrix[i][j] : 0;
                }
                System.out.println();
            }

            System.out.println("Accuracy = " + numCorrect / (numCorrect + numWrong));
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    private static void testAndScore(int[][] confusionMatrix, NNearestNeighbor<ExemplarInterface<Double, Integer>> nearestNeighbor,
            ObservationProvider<Double, ExemplarInterface<Double, Integer>> testSetProvider) {
        final DiscreteExemplarFactory discreteExemplarFactory = new DiscreteExemplarFactory();
        while (testSetProvider.hasNext()) {
            final ExemplarInterface<Double, Integer> next = testSetProvider.next();
            DiscreteExemplar<Double> exemplar = discreteExemplarFactory.create(next.getFeatures());
            int classification = nearestNeighbor.classify(next);
            confusionMatrix[exemplar.getTarget()][classification]++;
        }
    }

}
