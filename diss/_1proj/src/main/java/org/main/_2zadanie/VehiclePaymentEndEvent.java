package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehiclePaymentEndEvent extends VehicleEvent {
    @Override
    public void execute() {
        stk.saveLeftVehicle(vehicle);
        stk.group1.freeWorker(vehicle);
        stk.averageVehicleTimeInSystem.vehicleLeft(vehicle);

        if (stk.queueAfterStk.getSize() > 0) {
            stk.scheduleStartPayment();
        } else if (stk.queueBeforeStk.getSize() > 0) {
            stk.scheduleReceiveVehicle();
        }
    }

    public VehiclePaymentEndEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
