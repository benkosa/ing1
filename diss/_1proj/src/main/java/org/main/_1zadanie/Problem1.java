package org.main._1zadanie;

import javax.swing.*;

public class Problem1 extends Problem {
    public Problem1 (int seed, long replications, int offset, int max_chart, String chartTitle, JLabel replication,  JLabel result) {
        super(seed, replications, offset, max_chart, chartTitle, replication,  result);
        expectedLength = 125;
        this.go();
    }

    /**
     * 1. Nech skupina idúca trasu B príde na miesto M presne o 13:00. Ako dlho
     * bude čakať na príchod skupiny idúcej po trase A, ak táto vyrazila na
     * túru o 10:55?
     * <p>
     * A trasa zacina o 10:55
     * aka j e p ze dorazi do ciela o 13:00 a viac
     * <p>
     * od 10:55 - 13:00 125 min
     * ak vygenerujem < 125 nemusime cakat
     * else musime
     */
    @Override
    public double onePass() {
        passes+=1;
        final int length = a_b.sample() + b_c + c_m.sample();
        if (length > expectedLength) {
            success+= length - expectedLength;
        }
        final double result = (double)success/passes;
        updateGui.updateResults(result, passes);
        return result;
    }
}
