package org.main.shared.Distribution;

import java.util.Random;

public class SeedGenerator extends BasicDistribution<Integer> {
    Random genSeed;

    public SeedGenerator(int seed) {
        this.genSeed = new Random(seed);
    }

    @Override
    public Integer sample() {
        return genSeed.nextInt();
    }
}
