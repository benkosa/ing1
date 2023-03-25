package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleInspectionEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleEndInspection(vehicle);
        //ak je volny vorker zo skupiny 2 a cakaju auta na inspekciu
        if (stk.group2.isWorkerFree() && stk.isWaitingCarForInspection()) {
            final Vehicle newVehicle = stk.queueInStk.poll();
            stk.group2.hireWorker();
            stk.scheduleStartInspection(newVehicle);
        }
    }

    public VehicleInspectionEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
