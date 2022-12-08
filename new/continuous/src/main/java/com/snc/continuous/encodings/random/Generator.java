package com.mangione.continuous.encodings.random;

import java.util.Random;

abstract public class Generator<T> {
    private final Random random;

    Generator(Random random){
        this.random = random;
    }

    abstract public T next();

    double exponential(){
        return -Math.log(random.nextDouble());
    }

    int Bernoulli(double q){
        return random.nextDouble()<=q?1:0;
    }


    int uniform(int size){
        return (int) Math.floor(size*random.nextDouble());
    }

    double uniform(){
        return random.nextDouble();
    }

    double uniform(double x, double y){
        return x + (y-x) * uniform();
    }
}
