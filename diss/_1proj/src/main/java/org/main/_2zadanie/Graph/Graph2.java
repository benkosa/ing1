package org.main._2zadanie.Graph;

import org.main._2zadanie.STK;

public class Graph2 extends Graph{

    int workers1;
    int seed;
    long replications;
    @Override
    public double onePass(int i) {
        STK stk = new STK(replications, 8*60*60, seed, workers1, i);
        stk.simulationStartSingleThread();
        return stk.averageVehiclesInSTK.totalResult();
    }

    public Graph2(int start, int end, long replications, int seed, int workers1) {
        super(start, end);
        this.replications = replications;
        this.seed = seed;
        this.workers1 = workers1;
    }
}
