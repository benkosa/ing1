package org.main.shared.EventSimulation;

public abstract class EventSimulation implements Comparable<EventSimulation> {
    protected double eventTime;
    protected EventSimulationCore myCore;
    public EventSimulation(double eventTime, EventSimulationCore myCore) {
        this.eventTime = eventTime;
        this.myCore = myCore;
    }
    public abstract void execute();
    @Override
    public int compareTo(EventSimulation o) {
        return Double.compare(this.eventTime, o.eventTime);
    }
}
