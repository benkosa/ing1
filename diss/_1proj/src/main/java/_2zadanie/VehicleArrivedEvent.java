package _2zadanie;

import shared.EventSimulation.EventSimulationCore;

public class VehicleArrivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.saveArrivedVehicle(vehicle);
        stk.scheduleNewArrival();
        vehicle.setArrived(stk.getCurrentTime());

        // je volny worker
        if (stk.group1.isWorkerFree()) {
            //niekto caka na platbu
            if (stk.queueAfterStk.getSize() > 0) {
                final Vehicle newVehicle = stk.queueAfterStk.poll();
                stk.group1.hireWorker(newVehicle);
                stk.scheduleStartPayment(newVehicle);
            // ideme priamo na receive
            } else if (stk.isSpaceInsideStk() && stk.queueBeforeStk.getSize() == 0){
                vehicle.startWaitingInQue = stk.getCurrentTime();
                stk.averageWaitingBeforeSTK.countAverageTimeInQueue(vehicle.startWaitingInQue);
                stk.group1.hireWorker(this.vehicle);
                stk.arrivedInStkQueue(this.vehicle);
                stk.scheduleReceiveVehicle(this.vehicle);
                stk.averageQueueBeforeSTK.countAverageQueueLength();

            }else if (stk.isSpaceInsideStk() && stk.queueBeforeStk.getSize() > 0) {
                final Vehicle newVehicle = stk.queueBeforeStk.poll();
                stk.group1.hireWorker(newVehicle);
                stk.arrivedInStkQueue(newVehicle);
                stk.scheduleReceiveVehicle(newVehicle);

                vehicle.arrivedInQueue(stk.getCurrentTime());
                stk.queueBeforeStk.addQueue(vehicle);
            } else  {
                vehicle.arrivedInQueue(stk.getCurrentTime());
                stk.queueBeforeStk.addQueue(vehicle);
            }

        } else {
            vehicle.arrivedInQueue(stk.getCurrentTime());
            stk.queueBeforeStk.addQueue(vehicle);
        }
    }

    public VehicleArrivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
