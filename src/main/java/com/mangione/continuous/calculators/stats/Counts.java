package com.mangione.continuous.calculators.stats;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class Counts {
    private Map<String, Count> countMap = new HashMap<>();

    public List<Count> getCounts() {
        ArrayList<Count> counts = new ArrayList<>(countMap.values());
        counts.sort((o1, o2) -> o2.getCount() - o1.getCount());
        return counts;
    }

    public void add(String key) {
        countMap.putIfAbsent(key, new Count(key));
        countMap.get(key).add();
    }
}
