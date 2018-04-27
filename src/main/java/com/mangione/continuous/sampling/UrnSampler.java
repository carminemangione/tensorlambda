package com.mangione.continuous.sampling;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class UrnSampler {
    private boolean replace;
    private Random random;
    private int[] counts;
    private int count;

    public UrnSampler(boolean replace,Random random, int... counts) {
        this.replace = replace;
        this.random = random;
        this.counts = Arrays.copyOf(counts,counts.length);
        this.count = IntStream.of(counts).sum();
    }

    private void remove(int index){
        if(counts[index]>0) {
            counts[index]-=1;
            count-=1;
        }
        else throw new IllegalStateException(String.format("Negative count at index %d.",index));
    }

    public int pick(){
        double r = random.nextDouble()*this.count;
        int index = 0;
        while(r>0){
            r-=counts[index];
            index++;
        }
        index--;
        if(!replace)remove(index);
        return index;
    }

    public boolean hasNext(){
        return count>0;
    }

    int[] getCounts(){
        return counts;
    }

    public static void main(String[] args) {
        Random random = new Random(1305385);
        UrnSampler urn = new UrnSampler(true, random, 2, 2);
    }

    @Override
    public String toString() {
        return "UrnSampler{" +
                "counts=" + Arrays.toString(counts) +
                ", count=" + count +
                '}';
    }
}
