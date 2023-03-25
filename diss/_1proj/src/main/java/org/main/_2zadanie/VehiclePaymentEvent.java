package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehiclePaymentEvent extends VehicleEvent {
    @Override
    public void execute() {
        stk.scheduleEndPayment(this.vehicle);
        // ak niekto caka na platbu a je volny zamestanec zo skupiny 1
        if (stk.queueAfterStk.size() > 0 && stk.group1.isWorkerFree()) {
            final Vehicle newVehicle = stk.queueAfterStk.poll();
            stk.group1.hireWorker();
            stk.scheduleStartPayment(newVehicle);
        // ak niekto caka pred stk a je volny zamestnace zo skupiny 1 a je volne miesto na parkovisku
        } else if (
                stk.queueBeforeStk.size() > 0 &&
                stk.isSpaceInsideStk() &&
                stk.group1.isWorkerFree()
        ) {
            final Vehicle newVehicle = stk.queueBeforeStk.poll();
            stk.group1.hireWorker();
            stk.arrivedInStkQueue(newVehicle);
            stk.scheduleReceiveVehicle(newVehicle);
        }
    }

    public VehiclePaymentEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
