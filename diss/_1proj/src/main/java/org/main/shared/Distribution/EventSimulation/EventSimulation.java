package org.main.shared.Distribution.EventSimulation;

public abstract class EventSimulation {
    protected long eventTime;
    protected EventSimulationCore myCore;

    public EventSimulation(long eventTime, EventSimulationCore myCore) {
        this.eventTime = eventTime;
        this.myCore = myCore;
    }

    public abstract void execute();
}
