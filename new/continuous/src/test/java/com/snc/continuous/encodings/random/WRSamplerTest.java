package com.mangione.continuous.encodings.random;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.*;

public class WRSamplerTest {
    @Test
    public void TestSampler(){
        Random rng = new Random(1305385);
        int repetitions = 100;
        int size = 100000;
        int resolution = 10;
        int modulus = size/resolution;
        double[] tally = new double[resolution];
        WRSampler sampler = new WRSampler(10000, size, rng);
        long startTime = System.nanoTime();
        for(int i=0;i<repetitions;i++){
            int[] sample=sampler.next();
            for(int v : sample){
                int index = v/modulus;
                tally[index]+=1.0;
            }
        }
        long elapssedTime = System.nanoTime()-startTime;
        for(double t: tally){
            System.out.println(t/repetitions);
        }
        System.out.println(elapssedTime*1.0e-9);
    }

}
