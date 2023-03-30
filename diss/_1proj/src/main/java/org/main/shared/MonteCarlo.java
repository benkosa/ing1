package org.main.shared;

import javax.swing.*;

public abstract class MonteCarlo extends SwingWorker<Double, Double> {
    protected long replications;
    protected boolean interrupt = false;

    @Override
    protected Double doInBackground() throws Exception {
        long i;
        double result = 0;
        this.beforeSimulation();
        for (i = 0; i < replications; i++) {
            beforeReplication();
            result = onePass();
            afterReplication();

            if (interrupt) {
                this.afterSimulation();
                return result;
            }
        }
        this.afterSimulation();

        return result;
    }

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
    protected abstract void beforeReplication();
    protected abstract void afterReplication();

    public double simulationStart() {
        execute();
        return 0;
    }


}
