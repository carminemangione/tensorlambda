package com.mangione.continuous.calculators.stats.counts;

@SuppressWarnings("WeakerAccess")
public class Count<K> {
    private K key;
    private int count;

    public Count(K key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public K getKey() {
        return key;
    }

    public void add() {
        count++;
    }
}
