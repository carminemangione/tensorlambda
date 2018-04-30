package com.mangione.assignments.assignment1;

import java.io.File;
import java.net.URL;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.mangione.continuous.observationproviders.CsvObservationProvider;
import com.mangione.continuous.sampling.SamplingWithoutReplacement;

public class Assignment1 {
    public static void main(String[] args) throws Exception {
        URL abaloneURL = Assignment1.class.getClassLoader().getResource("com/mangione/continuous/abalone/abalone.data");

        assert abaloneURL != null;
        long totalNumberOfLines =
                new CsvObservationProvider(new File(abaloneURL.toURI())).getNumberOfLines();

        MersenneTwister random = new MersenneTwister(198273);

        int[] numbersOfRuns = {10, 100, 1000, 10000, 10000};
        Stats[] allStats = new Stats[numbersOfRuns.length];

        for (int i = 0; i < numbersOfRuns.length; i++) {
            allStats[i] = getStats(totalNumberOfLines, random, numbersOfRuns[i]);
        }
        new XYRenderer(allStats, numbersOfRuns);
    }

    private static Stats getStats(long totalNumberOfLines, MersenneTwister random, int numberOfRuns) {
        double[] indexSelections = new double[(int)totalNumberOfLines];
        for (int i = 0; i < numberOfRuns; i++) {
            SamplingWithoutReplacement samplingWithoutReplacement = new SamplingWithoutReplacement(.10, totalNumberOfLines, random);
            for (int j = 0; j < totalNumberOfLines; j++) {
                if (samplingWithoutReplacement.select())
                    indexSelections[j]++;
            }
        }
        StandardDeviation sd = new StandardDeviation(false);
        Mean mean = new Mean();
        return new Stats(mean.evaluate(indexSelections) / numberOfRuns, sd.evaluate(indexSelections) / numberOfRuns);
    }

}
