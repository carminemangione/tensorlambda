package com.mangione.continuous.calculators.chisquare;

import org.junit.Test;

import static com.mangione.continuous.calculators.chisquare.ContingencyTableTest.createContingencyTableFromCounts;
import static org.junit.Assert.*;

public class ChiSquareTest {

    @Test
    // Taken from http://www.stat.yale.edu/Courses/1997-98/101/chisq.htm
    public void fromExample() {
        int[][] counts = {{49, 50, 69}, {24, 36, 38}, {19, 22, 28}};
        ContingencyTable table = createContingencyTableFromCounts(counts);

        ChiSquare chiSquare = new ChiSquare(table);
        assertEquals(1.51, chiSquare.getChiSquare(), 0.01);
    }

}