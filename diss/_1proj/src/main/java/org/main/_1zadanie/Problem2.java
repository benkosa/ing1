package org.main._1zadanie;

import javax.swing.*;

public class Problem2 extends Problem {
    public Problem2 (
            int seed,
            long replications,
            int offset,
            int max_chart,
            String chartTitle,
            JLabel replication,
            JLabel result) {
        super(seed, replications, offset, max_chart, chartTitle, replication,  result);
        this.expectedLength = 320;
        this.go();
    }

    /**
     * Aká je pravdepodobnosť, že ak skupina idúca trasu B vyrazí z miesta
     * D o 7:40 podarí sa im prísť na miesto stretnutia M do 13:00?
     * <p>
     * od 7:40 - 13:00 320min
     * <p>
     * ak vygenerovne je <= 330 stihli else neestihli
     */
    @Override
    protected double onePass() {
        passes+=1;
        final double length = d_e.sample() + e_c.sample() + c_m.sample();
        if (length <= expectedLength) {
            success+=1;
        }
        final double result = (double)success/passes;
        updateGui.updateResults(result, passes);
        return result;
    }
}
