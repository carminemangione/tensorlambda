package com.mangione.continuous.datagenerators;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

import com.mangione.continuous.observations.dense.ContinuousExemplar;

public class ContinuousExemplarGeneratorTest {

    private MersenneTwister twister;

    @Before
    public void setUp() {
        twister = new MersenneTwister(9834);
    }

    @Test
    public void testCorrectNumberOfExemplarsAndDimensions() {
        int numberOfDimensions = 12;
        int numberOfExemplars = 100;

        final double bias = 0.1;
        final double sdEpsilon = .001;

        NormalDistribution nd = new NormalDistribution(0, 1);
        double z = -bias / Math.sqrt(1 + sdEpsilon * sdEpsilon);
        final double cumulativeProbability = 1 - nd.cumulativeProbability(z);

        final int numberOfRuns = 10000;
        final double[] numPositives = {0};
        final double[] numberMiscategorized = {0};
        for (int i = 0; i < numberOfRuns; i++) {
            ContinuousExemplarGenerator continuousExemplarGenerator = new ContinuousExemplarGenerator(numberOfDimensions, numberOfExemplars, bias,
                    sdEpsilon, twister);
            final List<ContinuousExemplar> exemplars = continuousExemplarGenerator.getExemplars();
            assertEquals(numberOfExemplars, exemplars.size());
            exemplars.forEach(exemplar -> {
                numPositives[0] += exemplar.getLabel() > 0 ? 1 : 0;
                numberMiscategorized[0] += continuousExemplarGenerator.getNumberMiscategorized();
            });
        }
        double fractionOfPositives = numPositives[0] / (numberOfRuns * numberOfExemplars);
        System.out.println("fractionOfPositives = " + fractionOfPositives);
        System.out.println("fraction mis categorized = " + numberMiscategorized[0] / numberOfRuns);
        assertEquals(cumulativeProbability, fractionOfPositives, 0.01);

    }

}