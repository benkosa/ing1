package org.main._1zadanie;

import org.main.shared.Distribution.ContinuousUniformDistribution;
import org.main.shared.Distribution.DiscreteEmpiricalDistribution;
import org.main.shared.Distribution.DiscreteUniformDistribution;
import org.main.shared.MonteCarlo;

import javax.swing.*;
import java.util.Random;

public abstract class Problem  extends MonteCarlo {
    protected Random genSeed;
    protected ContinuousUniformDistribution d_e;
    protected DiscreteEmpiricalDistribution e_c;
    protected DiscreteEmpiricalDistribution c_m;
    protected DiscreteUniformDistribution a_b;
    protected int b_c;
    protected int expectedLength;
    protected long passes = 0;
    protected long success = 0;
    protected UpdateGui updateGui;

    public Problem(int seed, long REPLICATIONS, int OFFSET, int MAXIMUM_CHART_X, String CHART_TITLE, JLabel replication, JLabel result) {
        super(REPLICATIONS, OFFSET, MAXIMUM_CHART_X, CHART_TITLE);
        this.genSeed = new Random(seed);
        this.updateGui = new UpdateGui(replication, result);
        this.initialize();
    }

    protected void initialize() {
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
        a_b = new DiscreteUniformDistribution(genSeed, 39, 64);
        b_c = 57;
    }

    @Override
    public void beforeSimulation() {
        initialize();
    }

    @Override
    public void afterSimulation() { }
}
