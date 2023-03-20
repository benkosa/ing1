package org.main.shared.Statistics;
import org.main.shared.EventSimulation.EventSimulationCore;

public class AverageWaitingTimeInQueue extends Statistics{
    EventSimulationCore core;
    public AverageWaitingTimeInQueue(EventSimulationCore core) {
        this.core = core;
    }
    private double countTimeInQueue = 0;
    private long countCustomersInQueue = 0;
    public void countAverageTimeInQueue(double startWaitingInQue) {
        countTimeInQueue+= core.getCurrentTime()-startWaitingInQue;
        countCustomersInQueue+=1;
    }
    @Override
    protected double replicationResult() {
        return countTimeInQueue/countCustomersInQueue;
    }
    @Override
    public void initialize() {
        countTimeInQueue = 0;
        countCustomersInQueue = 0;
    }
}
