package org.main.shared.EventSimulation;

import org.main.shared.MonteCarlo;

import java.util.PriorityQueue;

public abstract class EventSimulationCore extends MonteCarlo{
    private final PriorityQueue<EventSimulation> timeLine = new PriorityQueue<>();

    public double getCurrentTime() {
        return currentTime;
    }

    private double currentTime;
    private final double maxTime;

    @Override
    protected double onePass() {
        simulate();
        return 0;
    }

    public EventSimulationCore(long replications, long maxTime) {
        super(replications);
        this.maxTime = maxTime;
        this.currentTime = 0;
    }

    public void addEvent(EventSimulation event) {
        if (event.eventTime < 0) {
            System.out.println("event time is less than 0");
            return;
        }
        event.eventTime += currentTime;
        this.timeLine.add(event);
    }

    public void simulate () {
        while (!timeLine.isEmpty() && currentTime <= maxTime) {
            final EventSimulation event = timeLine.poll();
            this.currentTime = event.eventTime;
            event.execute();
        }
    }

}
