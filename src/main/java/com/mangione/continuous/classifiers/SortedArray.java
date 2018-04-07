package com.mangione.continuous.classifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedArray<T extends Comparable<T>> {
    private final int size;
    private List<T> items = new ArrayList<>();

    public SortedArray(int size) {
        this.size = size;
    }

    public void add(T next) {
        int index = Collections.binarySearch(items, next);
        index = index >= 0 ? index : Math.abs(index) - 1;
        if (index < size) {
            items.add(index, next);
        }
    }

    public List<T> get() {
        return items.subList(0, size);
    }
}
