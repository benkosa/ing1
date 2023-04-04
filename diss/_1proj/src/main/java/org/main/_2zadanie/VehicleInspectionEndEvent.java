package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleInspectionEndEvent extends VehicleEvent {
    @Override
    public void execute() {
        stk.group2.freeWorker(vehicle);
        if (stk.queueInStk.getReadySize() > 0) {
            stk.scheduleStartInspection();
        }

        vehicle.arrivedInQueue(stk.getCurrentTime());
        stk.queueAfterStk.addQueue(vehicle);

        if (stk.group1.isWorkerFree()) {
            stk.scheduleStartPayment();
        }

    }

    public VehicleInspectionEndEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
