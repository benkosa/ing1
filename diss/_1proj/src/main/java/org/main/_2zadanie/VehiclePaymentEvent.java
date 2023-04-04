package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehiclePaymentEvent extends VehicleEvent {
    @Override
    public void execute() {

        vehicle = stk.queueAfterStk.poll();

        stk.group1.hireWorker(vehicle);
        stk.scheduleEndPayment(vehicle);
    }

    public VehiclePaymentEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
