package shared.Distribution;

import java.util.Random;

public class ContinuousUniformDistribution extends BasicDistribution<Double> {

    private final double tMin;
    private final double tMax;
    public ContinuousUniformDistribution(SeedGenerator genSeed, double tMin, double tMax) {
        this.genSeed = genSeed;
        this.genP = new Random(this.genSeed.sample());
        this.tMax = tMax;
        this.tMin = tMin;
    }

    @Override
    public Double sample()  {
        return continuousUniform(genP, tMin, tMax);
    }
}
