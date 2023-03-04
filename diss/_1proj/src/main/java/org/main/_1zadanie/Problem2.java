package org.main._1zadanie;

import org.main.shared.Distribution.ContinuousUniformDistribution;
import org.main.shared.Distribution.DiscreteEmpiricalDistribution;
import org.main.shared.MonteCarlo;

import javax.swing.*;
import java.util.Random;

public class Problem2 extends MonteCarlo {
    Random genSeed;
    ContinuousUniformDistribution d_e;
    DiscreteEmpiricalDistribution e_c;
    DiscreteEmpiricalDistribution c_m;

    long success = 0;
    long passes = 0;
    final int EXPECTED_LENGTH = 330;
    UpdateGui updateGui;

    public Problem2 (int seed, long replications, int offset, int max_chart, String chartTitle, JLabel replication, JLabel result) {
        super(replications, offset, max_chart, chartTitle);
        this.updateGui = new UpdateGui(replication, result);
        this.genSeed = new Random(seed);
        this.go();
    }


    /**
     * Aká je pravdepodobnosť, že ak skupina idúca trasu B vyrazí z miesta
     * D o 7:40 podarí sa im prísť na miesto stretnutia M do 13:00?
     * <p>
     * od 7:30 - 13:00 320min
     * <p>
     * ak vygenerovne je < 330 stihli else neestihli
     */
    @Override
    public double onePass() {
        passes+=1;
        final double length = d_e.sample() + e_c.sample() + c_m.sample();
        if (length < EXPECTED_LENGTH) {
            success+=1;
        }
        final double result = (double)success/passes;
        updateGui.updateResults(result, passes);
        return result;
    }

    @Override
    public void beforeSimulation() {
        d_e = new ContinuousUniformDistribution(genSeed, 19, 36);
        e_c = new DiscreteEmpiricalDistribution(
                new int[] {230, 244, 281 },
                new int[] {243, 280, 350 },
                new double[] {.3, .5, .2 },
                genSeed
        );
        c_m = new DiscreteEmpiricalDistribution(
                new int[] {3, 11, 21, 35, 53, 60, 96},
                new int[] {10, 20, 34, 52, 59, 95, 110},
                new double[] {.2, .2, .3, .1, .15, .03, .02 },
                genSeed
        );
    }

    @Override
    public void afterSimulation() {
        System.out.println((double)success/passes);
    }
}
