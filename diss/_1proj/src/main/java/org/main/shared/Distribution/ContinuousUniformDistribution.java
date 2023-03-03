package org.main.shared.Distribution;

import java.util.Random;

public class ContinuousUniformDistribution extends BasicDistribution<Double> {

    private final double tMin;
    private final double tMax;
    public ContinuousUniformDistribution(Random genSeed, double tMin, double tMax) {
        this.genSeed = genSeed;
        this.genP = new Random(this.genSeed.nextInt());
        this.tMax = tMax;
        this.tMin = tMin;
    }

    @Override
    public Double sample()  {
        return continuousUniform(genP, tMin, tMax);
    }
}
