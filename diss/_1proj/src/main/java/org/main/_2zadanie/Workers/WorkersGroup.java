package org.main._2zadanie.Workers;

import org.main._2zadanie.STK;

public abstract class WorkersGroup {
    private final int numberOfWorkers;

    private final STK stk;
    private int workersInUsage;


    public WorkersGroup(int numberOfWorkers, STK stk) {
        this.numberOfWorkers = numberOfWorkers;
        this.stk = stk;
    }

    /**
     *
     */
    public abstract void freeWorker();

    public boolean isWorkerFree() {
        return workersInUsage < numberOfWorkers;
    }


}
