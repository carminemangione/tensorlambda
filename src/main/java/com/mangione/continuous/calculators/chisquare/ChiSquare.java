package com.mangione.continuous.calculators.chisquare;

public class ChiSquare {
    private final double chiSquare;

    public ChiSquare(ContingencyTable table) {
        double chiSquare = 0;
        ExpectedValues expectedValues = new ExpectedValues(table);
        for (int observationIndex = 0; observationIndex < table.getNumberOfObservationStates(); observationIndex++) {
            for (int targetIndex = 0; targetIndex < table.getNumberOfTargetStates(); targetIndex++) {
                double expectedValue = expectedValues.expectedValue(
                        observationIndex, targetIndex);
                chiSquare += Math.pow(table.getCountForState(observationIndex, targetIndex) - expectedValue, 2)
                        / expectedValue;
            }
        }
        this.chiSquare = chiSquare;
    }

    public double getChiSquare() {
        return chiSquare;
    }

    @Override
    public String toString() {
        return "" + chiSquare;
    }
}
