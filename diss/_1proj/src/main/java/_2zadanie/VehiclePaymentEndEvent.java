package _2zadanie;

import shared.EventSimulation.EventSimulationCore;

public class VehiclePaymentEndEvent extends VehicleEvent {
    @Override
    public void execute() {
        stk.group1.freeWorker(vehicle);
        stk.saveLeftVehicle(vehicle);
        stk.averageVehiclesInSTK.vehicleLeft();
        stk.averageVehicleTimeInSystem.vehicleLeft(vehicle);

        // ak niekto caka na platbu a je volny zamestanec zo skupiny 1
        if (stk.queueAfterStk.getSize() > 0) {
            final Vehicle newVehicle = stk.queueAfterStk.poll();
            stk.group1.hireWorker(newVehicle);
            stk.scheduleStartPayment(newVehicle);
            // ak niekto caka pred stk a je volny zamestnace zo skupiny 1 a je volne miesto na parkovisku
        } else if (
                stk.queueBeforeStk.getSize() > 0 &&
                        stk.isSpaceInsideStk()
        ) {
            final Vehicle newVehicle = stk.queueBeforeStk.poll();
            stk.group1.hireWorker(newVehicle);
            stk.arrivedInStkQueue(newVehicle);
            stk.scheduleReceiveVehicle(newVehicle);
        }
    }

    public VehiclePaymentEndEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}