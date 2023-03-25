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
    public void freeWorker() {
        workersInUsage-=1;
        if (workersInUsage < 0) {
            System.out.println("error: workersInUsage < 0");
        }
    };

    public boolean isWorkerFree() {
        return workersInUsage < numberOfWorkers;
    }

    public void hireWorker() {
        workersInUsage+=1;
        if (workersInUsage > numberOfWorkers) {
            System.out.println("error: workersInUsage > numberOfWorkers");
        }
    }


}
