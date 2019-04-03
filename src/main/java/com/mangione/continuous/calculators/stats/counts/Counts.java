package com.mangione.continuous.calculators.stats.counts;

import java.util.*;

public class Counts<K> {
    private Map<K, Count<K>> countMap = new HashMap<>();

    public List<Count<K>> getCounts() {
        ArrayList<Count<K>> counts = new ArrayList<>(countMap.values());
        counts.sort((o1, o2) -> o2.getCount() - o1.getCount());
        return counts;
    }

    public void add(K key) {
        countMap.putIfAbsent(key, new Count<>(key));
        countMap.get(key).add();
    }
}
