package com.mangione.continuous.observations.sparse;

class CSRRow {
    private final double[] values;
    private final int[] indexes;

    CSRRow(int[] indexes, double[] values) {
        if (indexes == null || values == null)
            throw new IllegalArgumentException("indexes or values may not be null");
        if (indexes.length != values.length)
            throw new IllegalArgumentException("Number of indexes and values must be equal");
        this.values = values;
        this.indexes = indexes;
    }

    CSRRow(SparseObservation<? extends Number> observation) {
        indexes = observation.getColumnIndexes().stream()
                .mapToInt(i -> i).toArray();
        values = observation.getColumnIndexes().stream()
                .map(observation::getFeature)
                .mapToDouble(Number::doubleValue)
                .toArray();
    }

    double[] getValues() {
        return values;
    }

    int[] getIndexes() {
        return indexes;
    }

    int getNumValues() {
        return values.length;
    }


}
