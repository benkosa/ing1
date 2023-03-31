package org.main.shared.Statistics;

import org.main.shared.EventSimulation.EventSimulationCore;

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

public class AverageQueueLength extends Statistics {
    public AverageQueueLength(EventSimulationCore core, Collection queue) {
        this.core = core;
        this.queue = queue;
    }
    private final EventSimulationCore core;
    private final Collection queue;
    private double lastTimeChange = 0;
    private double countEventsInQueue = 0;
    public void countAverageQueueLength() {
        final double time = core.getCurrentTime() - lastTimeChange;
        if (time < 0) System.out.println("error: countAverageQueueSize - time < 0");

        countEventsInQueue += time*queue.size();
        lastTimeChange = core.getCurrentTime();
    }
    @Override
    public double replicationResult() {
        return countEventsInQueue / lastTimeChange;
    }
    public void initialize() {
        lastTimeChange = 0;
        countEventsInQueue = 0;
    }
}
