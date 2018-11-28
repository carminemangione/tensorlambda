package com.mangione.continuous.datagenerators;

import com.mangione.continuous.observations.DiscreteExemplar;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiscreteExemplarGeneratorTest {

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
            DiscreteExemplarGenerator discreteExemplarGenerator = new DiscreteExemplarGenerator(numberOfDimensions, numberOfExemplars, bias,
                    sdEpsilon, twister);
            final List<DiscreteExemplar<Double>> exemplars = discreteExemplarGenerator.getExemplars();
            assertEquals(numberOfExemplars, exemplars.size());
            exemplars.forEach(exemplar -> {
                numPositives[0] += exemplar.getTarget() > 0 ? 1 : 0;
                numberMiscategorized[0] += discreteExemplarGenerator.getNumberMiscategorized();
            });
        }
        double fractionOfPositives = numPositives[0] / (numberOfRuns * numberOfExemplars);
        System.out.println("fractionOfPositives = " + fractionOfPositives);
        System.out.println("fraction mis categorized = " + numberMiscategorized[0] / numberOfRuns);
        assertEquals(cumulativeProbability, fractionOfPositives, 0.01);

    }

}