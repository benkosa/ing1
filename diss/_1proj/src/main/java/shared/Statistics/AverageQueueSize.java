package shared.Statistics;

import shared.EventSimulation.Queue;

import java.util.LinkedList;

public class AverageQueueSize extends Statistics{

    LinkedList queue;
    public AverageQueueSize(LinkedList queue) {
        this.queue = queue;
    }

    @Override
    protected double replicationResult() {
        return queue.size();
    }

    @Override
    public void initialize() {

    }
}
