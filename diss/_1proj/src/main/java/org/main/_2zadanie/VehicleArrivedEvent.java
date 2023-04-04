package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleArrivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleNewArrival();
        stk.saveArrivedVehicle(vehicle);
        vehicle.setArrived(stk.getCurrentTime());

        // ak je niekto v rade na zaplatenie
        // a je volny dalsi zamestnanec zo skupiny 1
        if (stk.queueAfterStk.getSize() > 0 && stk.group1.isWorkerFree()) {
            final Vehicle newVehicle = stk.queueAfterStk.poll();
            stk.group1.hireWorker(newVehicle);
            stk.scheduleStartPayment(newVehicle);
            System.out.println(newVehicle.getStartWaitingInQue());
            // ak je auto v rade pred stk
            // a je miesto v rade na konrolu
            // a je volny dalsi zamestnanec zo skupiny 1
        } else if (
                stk.queueBeforeStk.getSize() > 0 &&
                        stk.isSpaceInsideStk() &&
                        stk.group1.isWorkerFree()
        ) {
            final Vehicle newVehicle = stk.queueBeforeStk.poll();
            stk.group1.hireWorker(newVehicle);
            stk.arrivedInStkQueue(newVehicle);
            stk.scheduleReceiveVehicle(newVehicle);
        } else {
            vehicle.arrivedInQueue(stk.getCurrentTime());
            stk.queueBeforeStk.addQueue(vehicle);
        }
    }

    public VehicleArrivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
