package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleReceivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleReceiveVehicleEnd(this.vehicle);
        // ak je niekto v rade na zaplatenie
        // a je volny dalsi zamestnanec zo skupiny 1
        if (stk.queueAfterStk.size() > 0 && stk.group1.isWorkerFree()) {
            final Vehicle newVehicle = stk.queueAfterStk.poll();
            stk.group1.hireWorker();
            stk.scheduleStartPayment(newVehicle);
        // ak je auto v rade pred stk a je miesto v rade na konrolu
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

    public VehicleReceivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
