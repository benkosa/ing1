package shared;

import javax.swing.*;

public abstract class MonteCarlo extends SwingWorker<Double, Double> {
    protected long replications;
    protected boolean interrupt = false;

    @Override
    protected Double doInBackground() throws Exception {
        return simulation();
    }

    private Double simulation() {
        long i;
        double result = 0;
        this.beforeSimulation();
        for (i = 0; i < replications; i++) {
            beforeReplication();
            result = onePass();
            afterReplication();

            if (interrupt) {
                return result;
            }
        }
        this.afterSimulation();

        return result;
    }

    public long getReplications() {
        return replications;
    }

    public MonteCarlo(
            final long REPLICATIONS
    ) {
        this.replications = REPLICATIONS;
    }

    public void stopSimulation() {
        cancel(true);
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
    public double simulationStartSingleThread() {
        simulation();
        return 0;
    }


}
