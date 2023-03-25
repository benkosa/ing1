package org.main.shared.Distribution;

import java.util.Random;

public class UniformDouble extends BasicDistribution<Double>{
    public UniformDouble(SeedGenerator seedGenerator) {
        this.genP = new Random(seedGenerator.sample());
    }

    @Override
    public Double sample() {
        return genP.nextDouble();
    }
}
