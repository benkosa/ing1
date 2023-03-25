package org.main.shared.Distribution;

import java.math.MathContext;
import java.util.Random;

/**
 * <a href="https://en.wikipedia.org/wiki/Exponential_distribution#Random_variate_generation">...</a>
 */
public class ExponentialDistribution extends BasicDistribution<Double> {
    double lambda;

    public ExponentialDistribution(SeedGenerator genSeed, double meanValue) {
        this.genP = new Random(genSeed.sample());
        this.lambda = (double)1/meanValue;
    }
    @Override
    public Double sample() {
        return -Math.log(genP.nextDouble())/lambda;
    }
}
