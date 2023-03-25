package org.main.shared.Distribution;

import java.util.Random;

public class TriangularDistribution extends BasicDistribution<Double>{
    final double fc;
    final double a;
    final double b;
    final double c;

    /**
     * <a href="https://en.wikipedia.org/wiki/Triangular_distribution#Generating_triangular-distributed_random_variates">...</a>
     * @param seedGenerator seed generator
     * @param a min
     * @param b max
     * @param c modus
     */
    public TriangularDistribution(SeedGenerator seedGenerator, double a, double b, double c) {
        this.genSeed = seedGenerator;
        this.a = a;
        this.b = b;
        this.c = c;
        this.genP = new Random(seedGenerator.sample());
        fc = (c - a) / (b - a);
    }

    @Override
    public Double sample() {
        final double U = genP.nextDouble();
        if (U < fc) {
            return a + Math.sqrt(U * (b - a) * (c - a));
        } else {
            return b - Math.sqrt((1 - U) * (b - a) * (b - c));
        }
    }
}
