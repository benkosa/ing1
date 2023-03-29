package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleArrivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleNewArrival();
        stk.saveArrivedVehicle(vehicle);
        // ak je volny woker a je miesto v rade na kontrolu
        if (stk.group1.isWorkerFree() && stk.isSpaceInsideStk()) {
            stk.group1.hireWorker();
            stk.arrivedInStkQueue(this.vehicle);
            stk.scheduleReceiveVehicle(this.vehicle);
        // vojdeme do radu pred stk
        } else {
            vehicle.arrivedInQueue(stk.getCurrentTime(), true);
            stk.queueBeforeStk.addQueue(vehicle);
        }
    }

    public VehicleArrivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
