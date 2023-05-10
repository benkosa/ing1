package _2zadanie.Graph;

import _2zadanie.STK;

public class Graph2 extends Graph{

    int workers1;
    int seed;
    long replications;
    @Override
    public double onePass(double i) {
        STK stk = new STK(replications, 8*60*60, seed, workers1, (int)i);
        stk.simulationStartSingleThread();
        return stk.averageVehicleTimeInSystem.totalResult()/60.;
    }

    public Graph2(int start, int end, long replications, int seed, int workers1) {
        super(start, end, "počte pracovníkov skupiny 2", "priemerny čas stráveny zákazníkom v prevádzke", "Graph 2");
        this.replications = replications;
        this.seed = seed;
        this.workers1 = workers1;
    }
}
