package com.mangione.continuous.observationproviders;

import java.util.ArrayList;
import java.util.HashSet;

import com.mangione.continuous.model.modelproviders.DataProvider;

public class SparseObservationProvider implements DataProvider<HashSet<Integer>> {

    private final ArrayList<HashSet<Integer>> sparseObservations;
    private final int lengthOfObs;

    public SparseObservationProvider(ArrayList<HashSet<Integer>> data, int lengthOfObs) {
        this.sparseObservations = data;
        this.lengthOfObs = lengthOfObs;
    }

    @Override
    public int getNumberOfLines() {
        return this.sparseObservations.size();
    }

    public HashSet<Integer> get(int index) {
        try {
            return sparseObservations.get(index);
        } catch (Throwable e) {
            System.out.println("Index: " + index);
            return null;
        }
    }

    @Override
    public int getLengthOfObservation() {
        return this.lengthOfObs;
    }

}