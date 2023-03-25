package org.main.shared.EventSimulation;

import org.main.shared.EventSimulation.EventSimulation;
import org.main.shared.EventSimulation.EventSimulationCore;

public class RealTimeEvent extends EventSimulation {

    private final int stepTime;
    private final int sleepTime;
    @Override
    public void execute() {
        eventTime = stepTime;
        myCore.addEvent(this);
        System.out.println(myCore.getCurrentTime());
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public RealTimeEvent(double eventTime, EventSimulationCore myCore, int sleepTime) {
        super(eventTime, myCore);
        this.sleepTime = sleepTime;
        this.stepTime = (int)eventTime;
    }
}
