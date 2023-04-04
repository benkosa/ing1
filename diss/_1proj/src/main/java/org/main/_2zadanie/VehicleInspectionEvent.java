package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleInspectionEvent extends VehicleEvent{
    @Override
    public void execute() {
        vehicle = stk.queueInStk.poll();

        if (vehicle == null) {
            System.out.println("warning: VehicleInspectionEvent vehicle not found");
            return;
        }

        stk.group2.hireWorker(vehicle);
        stk.scheduleEndInspection(vehicle);
    }

    public VehicleInspectionEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
