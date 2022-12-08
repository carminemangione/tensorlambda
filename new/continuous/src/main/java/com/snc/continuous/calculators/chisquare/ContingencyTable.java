package com.mangione.continuous.calculators.chisquare;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class ContingencyTable {
    public static final String INVALID_OBSERVATION_STATE = "Invalid observation state: ";
    public static final String INVALID_TARGET_STATE = "Invalid target state: ";
    private int[][] countsWithTotalsInLastRowAndColumns;
    private final int observationSumLocation;
    private final int targetSumLocation;

    private ContingencyTable(int[][] countsWithTotalsInLastRowAndColumns, int observationSumLocation, int targetSumLocation) {

        this.countsWithTotalsInLastRowAndColumns = countsWithTotalsInLastRowAndColumns;
        this.observationSumLocation = observationSumLocation;
        this.targetSumLocation = targetSumLocation;
    }

    public int getCountForState(int observationState, int targetState) {
        validateStates(observationState, targetState, observationSumLocation, targetSumLocation);
        return countsWithTotalsInLastRowAndColumns[observationState][targetState];
    }


    public int getTotalForObservationState(int state) {
        validateState(state, targetSumLocation, INVALID_OBSERVATION_STATE);
        return countsWithTotalsInLastRowAndColumns[state][observationSumLocation];
    }

    public int getTotalForTargetState(int state) {
        validateState(state, observationSumLocation, INVALID_TARGET_STATE);
        return countsWithTotalsInLastRowAndColumns[targetSumLocation][state];
    }

    public int getObservationCount() {
        return countsWithTotalsInLastRowAndColumns[targetSumLocation][observationSumLocation];
    }

    public int getNumberOfObservationStates() {
        return targetSumLocation;
    }

    public int getNumberOfTargetStates() {
        return observationSumLocation;
    }

    @Override
    public String toString() {
        return "ContingencyTable{" +
                "countsWithTotalsInLastRowAndColumns=" + Arrays.deepToString(countsWithTotalsInLastRowAndColumns) +
                '}';
    }

    public static class Builder {
        private final int[][] countsWithTotalsInLastRowAndColumns;
        private final int numberOfTargetStates;
        private final int numberOfObservationStates;

        public Builder(int numberOfObservationStates, int numberOfTargetStates) {
            if (numberOfObservationStates < 1)
                throw new IllegalArgumentException("Number of observation states must be greater than zero");

            if (numberOfTargetStates < 1)
                throw new IllegalArgumentException("Number of target states must be greater than zero");
            countsWithTotalsInLastRowAndColumns = new int[numberOfObservationStates + 1][numberOfTargetStates + 1];
            this.numberOfTargetStates = numberOfTargetStates;
            this.numberOfObservationStates = numberOfObservationStates;
        }

        public ContingencyTable build() {
            return new ContingencyTable(countsWithTotalsInLastRowAndColumns, numberOfTargetStates, numberOfObservationStates);
        }

        public Builder addObservation(int observationState, int targetState) {
            validateStates(observationState, targetState, numberOfTargetStates, numberOfObservationStates);

            countsWithTotalsInLastRowAndColumns[observationState][targetState]++;
            countsWithTotalsInLastRowAndColumns[observationState][numberOfTargetStates]++;
            countsWithTotalsInLastRowAndColumns[numberOfObservationStates][targetState]++;
            countsWithTotalsInLastRowAndColumns[numberOfObservationStates][numberOfTargetStates]++;
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "countsWithTotalsInLastRowAndColumns=" + Arrays.deepToString(countsWithTotalsInLastRowAndColumns) +
                    '}';
        }
    }

    private static void validateStates(int observationState, int targetState, int observationSumLocation, int targetSumLocation) {
        validateState(observationState, targetSumLocation, INVALID_OBSERVATION_STATE);
        validateState(targetState, observationSumLocation, INVALID_TARGET_STATE);
    }

    private static void validateState(int state, int sumLocation, String s) {
        if (state >= sumLocation || state < 0)
            throw new IllegalArgumentException(s + state);
    }
}
