package org.main._2zadanie.Graph;

import org.main._2zadanie.STK;

public class Graph1  extends Graph{
    int workers2;
    int seed;
    long replications;
    @Override
    public double onePass(int i) {
        STK stk = new STK(replications, 8*60*60, seed, i, workers2);
        stk.simulationStartSingleThread();
        return stk.averageWaitingBeforeSTK.totalResult()/60;
    }

    public Graph1(int start, int end, long replications, int seed, int workers2) {
        super(start, end);
        this.replications = replications;
        this.seed = seed;
        this.workers2 = workers2;
    }
}
