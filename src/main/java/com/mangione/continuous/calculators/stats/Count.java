package com.mangione.continuous.calculators.stats;

@SuppressWarnings("WeakerAccess")
public class Count {
    private String key;
    private int count;

    public Count(String key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public String getKey() {
        return key;
    }

    public void add() {
        count++;
    }
}
