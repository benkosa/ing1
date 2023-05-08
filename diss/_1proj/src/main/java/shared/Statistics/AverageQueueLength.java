package shared.Statistics;

import shared.EventSimulation.EventSimulationCore;

import java.util.Collection;
import java.util.HashMap;

public class AverageQueueLength extends Statistics {
    public AverageQueueLength(Core core, Collection queue) {
        this.core = core;
        this.queue = queue;
    }
    public AverageQueueLength(Core core, HashMap queue) {
        this.core = core;
        this.hashmap = queue;
    }
    private final Core core;
    private Collection queue = null;
    private HashMap hashmap = null;
    private double lastTimeChange = 0;
    private double countEventsInQueue = 0;


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
        return countEventsInQueue/lastTimeChange;
    }
    public void initialize() {
        lastTimeChange = 0;
        countEventsInQueue = 0;
    }

}
