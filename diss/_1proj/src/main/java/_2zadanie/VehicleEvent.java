package _2zadanie;

import shared.EventSimulation.EventSimulation;
import shared.EventSimulation.EventSimulationCore;

public abstract class VehicleEvent extends EventSimulation {
    Vehicle vehicle;
    public final STK stk = (STK)this.myCore;
    public VehicleEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore);
        this.vehicle = vehicle;
    }


}
