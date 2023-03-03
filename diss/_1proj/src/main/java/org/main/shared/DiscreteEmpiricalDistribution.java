package org.main.shared;

import java.util.Random;

public class DiscreteEmpiricalDistribution extends BasicDistribution<Integer>{
    int[] tMin;
    int[] tMax;
    double[] p;
    Random[] genT;

    public DiscreteEmpiricalDistribution(
        int[] tMin,
        int[] tMax,
        double[] p,
        Random genSeed
    ) {
        this.tMin = tMin;
        this.tMax = tMax;
        this.p = p;
        this.genT = new Random[p.length];
        this.genSeed = genSeed;

        genP = new Random(genSeed.nextInt());

        double testP = 0;
        for (int i = 0; i < p.length; i++) {
            genT[i] = new Random(genSeed.nextInt());
            testP+= p[i];
        }
        if (testP != 1) System.out.println("error p must be 1 but is: " + testP);
    }

    @Override
    public Integer sample() {
        double p = genP.nextDouble();

        int i = 0;
        for (; i < this.p.length; i++) {
            p-= this.p[i];
            if (p <= 0) break;
        }
        return discreteUniform(
                genT[i],
                tMin[i],
                tMax[i]
        );
    }


}
