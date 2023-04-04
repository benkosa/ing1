package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleReceivedEndEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.group1.freeWorker(vehicle);

        if (stk.queueAfterStk.getSize() > 0) {
            stk.scheduleStartPayment();
        } else if (stk.queueBeforeStk.getSize() > 0) {
            stk.scheduleReceiveVehicle();
        }

        if (stk.group2.isWorkerFree()) {
            stk.scheduleStartInspection();

            vehicle.arrivedInQueue(stk.getCurrentTime());
            stk.queueInStk.move(vehicle.id);
        }
    }

    public VehicleReceivedEndEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
