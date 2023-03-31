package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleInspectionEndEvent extends VehicleEvent {
    @Override
    public void execute() {
        stk.group2.freeWorker(vehicle);
        //ak je volny vorker zo skupiny 2 a cakaju auta na inspekciu
        if (stk.group2.isWorkerFree() && stk.isWaitingCarForInspection()) {
            final Vehicle newVehicle = stk.queueInStk.poll();
            stk.group2.hireWorker(newVehicle);
            stk.scheduleStartInspection(newVehicle);
        }
        // ak je volny worker zo skupiny 1
        if (stk.group1.isWorkerFree()) {
            stk.group1.hireWorker(vehicle);
            stk.scheduleStartPayment(vehicle);
        } else {
            vehicle.arrivedInQueue(stk.getCurrentTime());
            stk.queueAfterStk.addQueue(vehicle);
        }
    }

    public VehicleInspectionEndEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
