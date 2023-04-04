package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleReceivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleReceiveVehicleEnd(this.vehicle);
    }

    public VehicleReceivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
