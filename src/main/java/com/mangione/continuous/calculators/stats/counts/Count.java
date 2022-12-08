package com.mangione.continuous.calculators.stats.counts;

import javax.annotation.Nonnull;

public class Count<K> implements Comparable<Count<K>> {
    private final K key;
    private final int count;

    public Count(K key, int count) {
        this.count = count;
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Count{" +
                "key=" + key +
                ", count=" + count +
                '}';
    }

    @Override
    public int compareTo(@Nonnull Count<K> o) {
        return Integer.compare(count, o.count);
    }
}
