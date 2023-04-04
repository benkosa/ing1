package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleInspectionEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleEndInspection(vehicle);
    }

    public VehicleInspectionEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
