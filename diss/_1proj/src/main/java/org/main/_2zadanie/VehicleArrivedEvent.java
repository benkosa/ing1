package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

public class VehicleArrivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleNewArrival();
        stk.saveArrivedVehicle(vehicle);
        vehicle.setArrived(stk.getCurrentTime());
        // ak je volny woker a je miesto v rade na kontrolu
        if (stk.group1.isWorkerFree() && stk.isSpaceInsideStk()) {
            vehicle.startWaitingInQue = stk.getCurrentTime();
            stk.averageWaitingBeforeSTK.countAverageTimeInQueue(vehicle.startWaitingInQue);
            stk.group1.hireWorker(this.vehicle);
            stk.arrivedInStkQueue(this.vehicle);
            stk.scheduleReceiveVehicle(this.vehicle);
            stk.averageQueueBeforeSTK.countAverageQueueLength();
        // vojdeme do radu pred stk
        } else {
            vehicle.arrivedInQueue(stk.getCurrentTime());
            stk.queueBeforeStk.addQueue(vehicle);
        }
    }

    public VehicleArrivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
