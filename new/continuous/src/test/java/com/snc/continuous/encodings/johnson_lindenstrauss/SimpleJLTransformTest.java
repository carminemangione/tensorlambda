package com.mangione.continuous.encodings.johnson_lindenstrauss;

import com.mangione.continuous.encodings.random.WRSampler;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
javax.annotation.Nonnull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class SimpleJLTransformTest {

    @Test
    public void testSimpleJLTransform(){
        Random random = new Random(1305385);
        int d = 32;
        int k = 4;
        JLTransformBuilder builder = new JLTransformBuilder(d, k,0.5, random);
        int trials = 10000;
        WRSampler sampler = new WRSampler(6, d,random);
        DMatrixRMaj e1 = generateRandomVector(d, sampler);
        DMatrixRMaj e2 = generateRandomVector(d, sampler);
        double dot = CommonOps_DDRM.dot(e1,e2);
        double sum=0.0;
        for(int u = 0; u< trials; u++){
            SimpleJLTransform xForm = builder.createSimpleJLTransform(d);
            sum+=CommonOps_DDRM.dot(xForm.apply(e1), xForm.apply(e2));
        }
        sum/=trials*d*k;
        Assert.assertEquals(dot,sum,10.0/Math.sqrt(trials));
    }

    @NotNull
    private DMatrixRMaj generateRandomVector(int d, WRSampler sampler) {
        int[] sample = sampler.next();
        DMatrixRMaj e = new DMatrixRMaj(1, d);
        for(int i: sample){
            e.set(0,i,1.0);
        }
        return e;
    }

}