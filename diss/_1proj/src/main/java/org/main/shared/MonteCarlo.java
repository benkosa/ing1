package org.main.shared;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.main._1zadanie.UpdateGui;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public abstract class MonteCarlo {
    protected long replications;
    protected boolean interrupt = false;


    public MonteCarlo(
            final long REPLICATIONS
    ) {
        this.replications = REPLICATIONS;
    }

    public void stopChart() {
        this.interrupt = true;
    }

    protected abstract double onePass();
    protected abstract void beforeSimulation();
    protected abstract void afterSimulation();

    public double simulationStart() {
        this.beforeSimulation();
        long i ;
        double result = 0;
        for (i = 0; i < replications; i++) {

            result = onePass();

            if (interrupt) {
                this.afterSimulation();
                return result;
            }
        }
        this.afterSimulation();

        return result;
    }

}
