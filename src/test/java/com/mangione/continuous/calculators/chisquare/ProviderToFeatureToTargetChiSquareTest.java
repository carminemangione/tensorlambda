package com.mangione.continuous.calculators.chisquare;

import com.mangione.continuous.observationproviders.ObservationProvider;
import com.mangione.continuous.observations.ExemplarInterface;
import com.mangione.continuous.observations.ObservationFactoryInterface;
import com.mangione.continuous.observations.dense.DiscreteExemplar;
import com.mangione.continuous.observations.sparse.SparseExemplar;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class ProviderToFeatureToTargetChiSquareTest {
    @Test
    public void oneFeatureOneTarget() {
        int[][] counts = {{4, 1}, {2, 3}};
        ContingencyTable table = ContingencyTableTest.createContingencyTableFromCounts(counts);
        ChiSquare baseChiSquare = new ChiSquare(table);

        List<DiscreteExemplar> exemplars = Arrays.asList(
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(0, 0)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(0, 0)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(0, 0)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(0, 0)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(0, 1)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(1, 0)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(1, 0)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(1, 1)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(1, 1)),
            DiscreteExemplar.getExemplarTargetLastColumn(Arrays.asList(1, 1)));




    }

    private SparseExemplar<Integer> getSparseExemplarWithTargetLastColumn(int numColumns) {
         return null;
    }






}