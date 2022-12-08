package com.mangione.continuous.datagenerators;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.dense.ContinuousExemplar;

public class ScaledContinuousExemplarGeneratorTest {
    private MersenneTwister twister;

   @Before
    public void setUp() {
        twister = new MersenneTwister(9834);
    }

    @Test
    public void testScale() {
        int numberOfExemplars = 100000;
        double[][] featureMeanAndSD = {{100, 30}, {2, 0.05}, {1000, 50}, {759, 20}};

        final double bias = 0.20;
        final double sdEpsilon = .001;

        DescriptiveStatistics[] stats = {new DescriptiveStatistics(), new DescriptiveStatistics(), new DescriptiveStatistics(), new DescriptiveStatistics()};
        ScaledContinuousExemplarGenerator sdeg = new ScaledContinuousExemplarGenerator(numberOfExemplars, featureMeanAndSD, bias, sdEpsilon, twister);
        List<ContinuousExemplar> exemplars = sdeg.generateDataSet();
        exemplars.forEach(exemplar -> {
            for (int i = 0; i < exemplar.getFeatures(Double[]::new).length; i++) {
                stats[i].addValue(exemplar.getFeatures(Double[]::new)[i]);
            }
        });

        for (int i = 0; i < featureMeanAndSD.length; i++) {
            assertEquals(featureMeanAndSD[i][0], stats[i].getMean(), 0.1);
            assertEquals(featureMeanAndSD[i][1], stats[i].getStandardDeviation(), 0.1);
        }
    }

    public static void main(String[] args) throws IOException {
        int numberOfExemplars = 10000;
        double[][] featureMeanAndSD = {{100, 30}, {2, 0.05}, {1000, 50}, {7, 1}, {0.01, 0.0001}, {500, 70}, {20, 1}, {3000, 50}, {10, 3}};
        MersenneTwister twister = new MersenneTwister(9834);

        ScaledContinuousExemplarGenerator sdeg = new ScaledContinuousExemplarGenerator(numberOfExemplars, featureMeanAndSD, 0.1, 0.0, twister);
        writeFile("Seperable.csv", sdeg.generateDataSet());

        sdeg = new ScaledContinuousExemplarGenerator(numberOfExemplars, featureMeanAndSD, 0.1, 0.01, twister);
        writeFile("3percent-miscategorization.csv", sdeg.generateDataSet());

        sdeg = new ScaledContinuousExemplarGenerator(numberOfExemplars, featureMeanAndSD, 0.1, 0.04, twister);
        writeFile("10percent-miscatergorization.csv", sdeg.generateDataSet());

    }

    private static void writeFile(String fileName, List<ContinuousExemplar> exemplars) throws IOException {
        BufferedWriter bos = new BufferedWriter(new FileWriter(
                "/Users/carmine/projects/continuous/src/test/java/com/mangione/continuous/assignments/assignment2/" + fileName));
        exemplars.forEach(exemplar->{
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < exemplar.getFeatures(Double[]::new).length; i++) {
                sb.append(exemplar.getFeatures(Double[]::new)[i]).append(',');
            }
            sb.append(exemplar.getLabel());
            try {
                bos.write(sb.toString());
                bos.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bos.close();
    }
}