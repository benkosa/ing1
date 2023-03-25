package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulation;
import org.main.shared.EventSimulation.EventSimulationCore;

public abstract class VehicleEvent extends EventSimulation {
    Vehicle vehicle;
    public final STK stk = (STK)this.myCore;
    public VehicleEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore);
        this.vehicle = vehicle;
    }


}
