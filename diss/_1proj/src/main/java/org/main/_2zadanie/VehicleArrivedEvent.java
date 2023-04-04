package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleArrivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.saveArrivedVehicle(vehicle);
        stk.scheduleNewArrival();

        stk.averageQueueBeforeSTK.countAverageQueueLength();

        vehicle.arrivedInQueue(stk.getCurrentTime());
        stk.queueBeforeStk.addQueue(vehicle);

        if (stk.group1.isWorkerFree()) {
            stk.scheduleReceiveVehicle();
        }
    }

    public VehicleArrivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
