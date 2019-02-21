package com.mangione.continuous.calculators.chisquare;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ContingencyTableTest {

    @Test
    public void twoStatesNoObservations() {
        int numberOfObservationStates = 2;
        int numberOfTargetStates = 2;

        ContingencyTable.Builder builder = new ContingencyTable.Builder(numberOfObservationStates, numberOfTargetStates);
        ContingencyTable table = builder.build();
        assertEquals(0, table.getCountForState(0, 0));
        assertEquals(0, table.getCountForState(1, 0));
        assertEquals(0, table.getCountForState(0, 1));
        assertEquals(0, table.getCountForState(1, 1));


        assertEquals(0, table.getTotalForObservationState(0));
        assertEquals(0, table.getTotalForObservationState(1));
        assertEquals(0, table.getTotalForTargetState(0));
        assertEquals(0, table.getTotalForTargetState(1));

        assertEquals(0, table.getObservationCount());
    }

    @Test
    // Taken from http://www.stat.yale.edu/Courses/1997-98/101/chisq.htm
    public void threeStatesBunchOfValues() {
        ContingencyTable.Builder builder = new ContingencyTable.Builder(3, 3);

        int[][] counts = {{49, 50, 69}, {24, 36, 38}, {19, 22, 28}};
        for (int observationState = 0; observationState < 3; observationState++)
            for (int targetState = 0; targetState < 3; targetState++)
                builder = addNumberOfObservationsForState(observationState, targetState, builder,
                        counts[observationState][targetState]);

        ContingencyTable table = builder.build();
        for (int observationState = 0; observationState < 3; observationState++)
            for (int targetState = 0; targetState < 3; targetState++)
                assertEquals(counts[observationState][targetState], table.getCountForState(observationState, targetState));

            assertEquals(168, table.getTotalForObservationState(0));
            assertEquals(98, table.getTotalForObservationState(1));
            assertEquals(69, table.getTotalForObservationState(2));

            assertEquals(92, table.getTotalForTargetState(0));
            assertEquals(108, table.getTotalForTargetState(1));
            assertEquals(135, table.getTotalForTargetState(2));

            assertEquals(335, table.getObservationCount());
    }

    @Test
    public void nonSymmetricStates() {
        ContingencyTable.Builder builder = new ContingencyTable.Builder(3, 2);

        int[][] counts = {{49, 50}, {24, 36}, {19, 22}};
        for (int observationState = 0; observationState < 3; observationState++)
            for (int targetState = 0; targetState < 2; targetState++)
                builder = addNumberOfObservationsForState(observationState, targetState, builder,
                        counts[observationState][targetState]);

        ContingencyTable table = builder.build();
        for (int observationState = 0; observationState < 3; observationState++)
            for (int targetState = 0; targetState < 2; targetState++)
                assertEquals(counts[observationState][targetState], table.getCountForState(observationState, targetState));

        assertEquals(99, table.getTotalForObservationState(0));
        assertEquals(60, table.getTotalForObservationState(1));
        assertEquals(41, table.getTotalForObservationState(2));

        assertEquals(92, table.getTotalForTargetState(0));
        assertEquals(108, table.getTotalForTargetState(1));

        assertEquals(200, table.getObservationCount());    }


    private ContingencyTable.Builder addNumberOfObservationsForState(int observationState, int targetState, ContingencyTable.Builder builder, int numberOfTimes) {
        ContingencyTable.Builder nextBuilder = builder;
        for (int i = 0; i < numberOfTimes; i++)
            nextBuilder = builder.addObservation(observationState, targetState);

        return nextBuilder;
    }


    @Test(expected = IllegalArgumentException.class)
    public void targetStateOutOfBoundsAdd() {
        new ContingencyTable.Builder(3, 3)
                .addObservation(0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observationStateOutOfBoundsAdd() {
        new ContingencyTable.Builder(3, 3)
                .addObservation(3, 0);

    }

    @Test(expected = IllegalArgumentException.class)
    public void targetStateOutOfBoundsGet() {
        new ContingencyTable.Builder(3, 3)
                .build()
                .getCountForState(0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observationStateOutOfBoundsGet() {
        new ContingencyTable.Builder(3, 3)
                .build()
                .getCountForState(3, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetStateNegativeAdd() {
        new ContingencyTable.Builder(3, 3)
                .addObservation(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observationStateNegativeAdd() {
        new ContingencyTable.Builder(3, 3)
                .addObservation(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetStateNegativeGet() {
        new ContingencyTable.Builder(3, 3)
                .build()
                .getCountForState(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observationStateNegativeGet() {
        new ContingencyTable.Builder(3, 3)
                .build()
                .getCountForState(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroTargetStatesExcepts()  {
        new ContingencyTable.Builder(3, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroObservationStatesExcepts()  {
        new ContingencyTable.Builder(0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetStateOutOfBoundsTargetTotal() {
        new ContingencyTable.Builder(3, 3)
                .build()
                .getTotalForTargetState(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observationStateOutOfBoundsObservationTotal() {
        new ContingencyTable.Builder(3, 3)
                .build()
                .getTotalForObservationState(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void targetStateNegativeTargetTotal() {
        new ContingencyTable.Builder(3, 3)
                .build()
                .getTotalForTargetState(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void observationStateNegativeObservationTotal() {
        new ContingencyTable.Builder(3, 3)
                .build()
                .getTotalForObservationState(-1);
    }



}