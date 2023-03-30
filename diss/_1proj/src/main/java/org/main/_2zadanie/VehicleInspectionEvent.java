package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleInspectionEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleEndInspection(vehicle);
        //ak je volny vorker zo skupiny 2 a cakaju auta na inspekciu
        if (stk.group2.isWorkerFree() && stk.isWaitingCarForInspection()) {
            final Vehicle newVehicle = stk.queueInStk.poll();
            stk.group2.hireWorker();
            stk.scheduleStartInspection(newVehicle);
        }
        // ak je niekto v rade na zaplatenie
        // a je volny dalsi zamestnanec zo skupiny 1
        if (stk.queueAfterStk.getSize() > 0 && stk.group1.isWorkerFree()) {
            final Vehicle newVehicle = stk.queueAfterStk.poll();
            stk.group1.hireWorker();
            stk.scheduleStartPayment(newVehicle);
            // ak je auto v rade pred stk
            // a je miesto v rade na konrolu
            // a je volny dalsi zamestnanec zo skupiny 1
        } else if (
                stk.queueBeforeStk.getSize() > 0 &&
                        stk.isSpaceInsideStk() &&
                        stk.group1.isWorkerFree()
        ) {
            final Vehicle newVehicle = stk.queueBeforeStk.poll();
            stk.group1.hireWorker();
            stk.arrivedInStkQueue(newVehicle);
            stk.scheduleReceiveVehicle(newVehicle);
        }
    }

    public VehicleInspectionEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
