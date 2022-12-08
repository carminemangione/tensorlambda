package com.mangione.continuous.calculators.chisquare;

@SuppressWarnings("WeakerAccess")
public class ExpectedValues {
    private ContingencyTable table;

    public ExpectedValues(ContingencyTable table) {
        this.table = table;
    }

    public double expectedValue(int observationState, int targetState) {
        return ((double)(table.getTotalForObservationState(observationState)
                * table.getTotalForTargetState(targetState)))
                / table.getObservationCount();
    }
}
