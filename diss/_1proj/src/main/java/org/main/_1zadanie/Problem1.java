package org.main._1zadanie;

import org.main.shared.Distribution.DiscreteEmpiricalDistribution;
import org.main.shared.Distribution.DiscreteUniformDistribution;
import org.main.shared.MonteCarlo;

import java.util.Random;

public class Problem1 extends MonteCarlo {
    Random genSeed;
    DiscreteEmpiricalDistribution c_m;
    DiscreteUniformDistribution a_b;
    int b_c;

    long waitingTime = 0;
    long passes = 0;
    final int EXPECTED_LENGTH = 125;

    public Problem1 (Random genSeed) {
        this.MonteCarlo(10000000000L, 600000, 500, "1. problem");
        this.genSeed = genSeed;
        this.go();
    }


    /**
     * 1. Nech skupina idúca trasu B príde na miesto M presne o 13:00. Ako dlho
     * bude čakať na príchod skupiny idúcej po trase A, ak táto vyrazila na
     * túru o 10:55?
     *
     * A trasa zacina o 10:55
     * aka j e p ze dorazi do ciela o 13:00 a viac
     *
     * od 10:55 - 13:00 125 min
     * ak vygenerujem < 125 stihli sme prist
     * else nestihli
     * @return
     */
    @Override
    public double onePass() {
        passes+=1;
        final int length = a_b.sample() + b_c + c_m.sample();
        if (length > EXPECTED_LENGTH) {
            waitingTime+= length - EXPECTED_LENGTH;
        }
        return (double)waitingTime/passes;
    }

    @Override
    public void beforeSimulation() {
        a_b = new DiscreteUniformDistribution(genSeed, 39, 64);
        b_c = 57;
        c_m = new DiscreteEmpiricalDistribution(
                new int[] {3, 11, 21, 35, 53, 60, 96},
                new int[] {10, 20, 34, 52, 59, 95, 110},
                new double[] {.2, .2, .3, .1, .15, .03, .02 },
                genSeed
        );
    }

    @Override
    public void afterSimulation() {
        System.out.println((double)waitingTime/passes);
    }

}
