package com.mangione.continuous.calculators.chisquare;

import org.junit.Test;

import static com.mangione.continuous.calculators.chisquare.ContingencyTableTest.createContingencyTableFromCounts;
import static org.junit.Assert.assertEquals;

public class ExpectedValuesTest {

    @Test
    // Taken from http://www.stat.yale.edu/Courses/1997-98/101/chisq.htm
    public void fromExample() {
        //
        int[][] counts = {{49, 50, 69}, {24, 36, 38}, {19, 22, 28}};
        ContingencyTable table = createContingencyTableFromCounts(counts);

        ExpectedValues expectedValues = new ExpectedValues(table);

        double[][] expectedExpectedValues = {{46.1, 54.2, 67.7}, {26.9, 31.6, 39.5}, {18.9, 22.2, 27.8}};

        for (int observationState = 0; observationState < 3; observationState++) {
            for (int targetState = 0; targetState < 3; targetState++) {
                assertEquals(expectedExpectedValues[observationState][targetState],
                        expectedValues.expectedValue(observationState, targetState), .1);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void observationIndexOutOfBounds()  {
        new ExpectedValues(new ContingencyTable.Builder(3, 5).build())
                .expectedValue(4, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetIndexOutOfBounds() {
        new ExpectedValues(new ContingencyTable.Builder(3, 5).build())
                .expectedValue(0, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetIndexNegative()  {
        new ExpectedValues(new ContingencyTable.Builder(3, 5).build())
                .expectedValue(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observationIndexNegative() {
        new ExpectedValues(new ContingencyTable.Builder(-1, 5).build())
                .expectedValue(-1, 0);
    }


}