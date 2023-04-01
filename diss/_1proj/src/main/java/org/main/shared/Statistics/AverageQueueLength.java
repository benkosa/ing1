package org.main.shared.Statistics;

import org.main.shared.EventSimulation.EventSimulationCore;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AverageQueueLength extends Statistics {
    public AverageQueueLength(EventSimulationCore core, Collection queue) {
        this.core = core;
        this.queue = queue;
    }
    public AverageQueueLength(EventSimulationCore core, HashMap queue) {
        this.core = core;
        this.hashmap = queue;
    }
    private final EventSimulationCore core;
    private Collection queue = null;
    private HashMap hashmap = null;
    private double lastTimeChange = 0;
    private double countEventsInQueue = 0;

    public final SampleStandardDeviation sampleStandardDeviation = new SampleStandardDeviation();
    public void countAverageQueueLength() {
        final double time = core.getCurrentTime() - lastTimeChange;
        if (time < 0) System.out.println("error: countAverageQueueSize - time < 0");
        if (queue != null)
            countEventsInQueue += time*queue.size();
        if (hashmap != null)
            countEventsInQueue += time*hashmap.size();
        lastTimeChange = core.getCurrentTime();
    }
    @Override
    public double replicationResult() {
        double result = (countEventsInQueue/lastTimeChange);
        sampleStandardDeviation.countReplication(result);
        return result;
    }
    public void initialize() {
        lastTimeChange = 0;
        countEventsInQueue = 0;
    }

}
