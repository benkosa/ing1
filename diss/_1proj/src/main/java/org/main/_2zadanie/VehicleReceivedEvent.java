package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleReceivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        vehicle = stk.queueAfterStk.poll();

        //naplanovanie zaciatku platby ak je niekto v rade
        if (stk.queueAfterStk.getSize() > 0) {
            stk.scheduleStartPayment();
            return;
        }

        //naplanovanie ukoncenia prevzatia vozidala
        if (stk.queueInStk.isSpaceInQueue()) {
            stk.averageQueueBeforeSTK.countAverageQueueLength();
            vehicle = stk.queueBeforeStk.poll();

            if (vehicle == null) {
                System.out.println("warning: VehicleReceivedEvent vehicle not found");
                return;
            }

            stk.group1.hireWorker(vehicle);
            stk.queueInStk.addQueueLocked(vehicle.id, vehicle);
            stk.scheduleReceiveVehicleEnd(vehicle);
        }

    }

    public VehicleReceivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
