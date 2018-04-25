package com.mangione.assignments.assignment2;

import com.mangione.continuous.classifiers.NNearestNeighbor;
import com.mangione.continuous.observationproviders.CsvObservationProvider;
import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observationproviders.SampledObservationProvider;
import com.mangione.continuous.observationproviders.VariableCalculator;
import com.mangione.continuous.observations.DiscreteExemplar;
import com.mangione.continuous.observations.Observation;
import com.mangione.continuous.observations.ObservationFactory;
import org.apache.commons.math3.random.MersenneTwister;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class Assignment2 {

    public static void main(String[] args) throws Exception {
        String[] files = {"Seperable.csv", "3percent-miscategorization.csv", "10percent-miscatergorization.csv"};

        for (String currentFile : files) {
            processSingleFile(currentFile);
        }
        processAbalone();
    }

    @SuppressWarnings("ConstantConditions")
    private static void processSingleFile(String currentFile) throws Exception {
        File file = new File(Assignment2.class.getClassLoader()
                .getResource("com/mangione/continuous/assignment2/" + currentFile).toURI());
        CsvObservationProvider<Observation> observationProvider = new CsvObservationProvider<>(file, new ObservationFactory());

        ObservationProvider<Observation> trainingSetProvider =
                new SampledObservationProvider(.10, observationProvider, new MersenneTwister(2123), false);
        int[] numNeighbors = {6, 12, 24, 36, 72};

        for (int numNeighbor : numNeighbors) {
            observationProvider.reset();

            int[][] confusionMatrix = new int[2][2];
            NNearestNeighbor nearestNeighbor = new NNearestNeighbor(trainingSetProvider, numNeighbor);

            observationProvider.reset();
            ObservationProvider<Observation> testSetProvider =
                    new SampledObservationProvider(.10, observationProvider, new MersenneTwister(2123), false);
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
            return out;
        });
        CsvObservationProvider<Observation> observationProvider = new CsvObservationProvider<>(file, new ObservationFactory(), calculators);

        ObservationProvider<Observation> trainingSetProvider =
                new SampledObservationProvider(.10, observationProvider, new MersenneTwister(2123), false);
        int[] numNeighbors = {6, 12, 24, 36, 72};

        for (int numNeighbor : numNeighbors) {
            observationProvider.reset();

            int[][] confusionMatrix = new int[30][30];
            NNearestNeighbor nearestNeighbor = new NNearestNeighbor(trainingSetProvider, numNeighbor);

            observationProvider.reset();
            ObservationProvider<Observation> testSetProvider =
                    new SampledObservationProvider(.10, observationProvider, new MersenneTwister(2123), false);

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

    private static void testAndScore(int[][] confusionMatrix, NNearestNeighbor nearestNeighbor, ObservationProvider<Observation> testSetProvider) throws Exception {
        while (testSetProvider.hasNext()) {
            final Observation next = testSetProvider.next();
            DiscreteExemplar exemplar = new DiscreteExemplar(next.getFeatures());
            int classification = nearestNeighbor.classify(next);
            confusionMatrix[exemplar.getTarget()][classification]++;
        }
    }

}
