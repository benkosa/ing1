package org.main.shared.Distribution;

import java.util.Random;

public class DiscreteUniformDistribution extends BasicDistribution<Integer> {
    private final int tMin;
    private final int tMax;
    public DiscreteUniformDistribution(SeedGenerator genSeed, int tMin, int tMax) {
        this.genSeed = genSeed;
        this.genP = new Random(this.genSeed.sample());
        this.tMax = tMax;
        this.tMin = tMin;
    }

    @Override
    public Integer sample() {
        return discreteUniform(genP, tMin, tMax);
    }

}
