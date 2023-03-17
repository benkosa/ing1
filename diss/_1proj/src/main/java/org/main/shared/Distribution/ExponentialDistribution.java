package org.main.shared.Distribution;

import java.math.MathContext;
import java.util.Random;

public class ExponentialDistribution extends BasicDistribution<Double> {
    double lambda;

    public ExponentialDistribution(Random genSeed, double meanValue) {
        this.genP = new Random(genSeed.nextInt());
        this.lambda = (double)1/meanValue;
    }
    @Override
    public Double sample() {
        return -Math.log(genP.nextDouble())/lambda;
    }
}
