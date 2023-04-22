package _2zadanie;

import shared.EventSimulation.EventSimulationCore;

public class VehicleReceivedEndEvent extends VehicleEvent{
    @Override
    public void execute() {
        vehicle.arrivedInQueue(stk.getCurrentTime());
        stk.queueInStk.move(vehicle.id);

        //ak je volny worker zp skupiny 2 a cakaju auta na inspekciu
        if (stk.group2.isWorkerFree()) {
            final Vehicle newVehicle = stk.queueInStk.poll();
            stk.group2.hireWorker(newVehicle);
            stk.scheduleStartInspection(newVehicle);
        }

        stk.group1.freeWorker(vehicle);
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

    public VehicleReceivedEndEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
