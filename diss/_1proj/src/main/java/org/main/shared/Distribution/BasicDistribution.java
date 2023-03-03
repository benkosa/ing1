package org.main.shared.Distribution;

import java.util.Random;

public abstract class BasicDistribution<T> {
    protected Random genSeed;
    protected Random genP;
    protected int discreteUniform(Random gen, int tMin, int tMax) {
        return gen.nextInt(tMin, tMax+1);
    }
    protected double continuousUniform(Random gen, double tMin, double tMax) {
        return gen.nextDouble(tMin, tMax);
    }
    abstract T sample();
}
