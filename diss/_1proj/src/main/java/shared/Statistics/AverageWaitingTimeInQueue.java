package shared.Statistics;
import shared.EventSimulation.EventSimulationCore;

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

    public void test(double startWaitingInQue) {
        countTimeInQueue+= core.getCurrentTime()-startWaitingInQue;
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
