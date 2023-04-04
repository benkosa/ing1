package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehiclePaymentEvent extends VehicleEvent {
    @Override
    public void execute() {
        stk.scheduleEndPayment(this.vehicle);
    }

    public VehiclePaymentEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
