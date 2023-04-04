package org.main._2zadanie;

import org.main.shared.EventSimulation.EventSimulationCore;

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
            } else if (stk.isSpaceInsideStk()){
                vehicle.startWaitingInQue = stk.getCurrentTime();
                stk.averageWaitingBeforeSTK.countAverageTimeInQueue(vehicle.startWaitingInQue);
                stk.group1.hireWorker(this.vehicle);
                stk.arrivedInStkQueue(this.vehicle);
                stk.scheduleReceiveVehicle(this.vehicle);
                stk.averageQueueBeforeSTK.countAverageQueueLength();
            } else  {
                vehicle.arrivedInQueue(stk.getCurrentTime());
                stk.queueBeforeStk.addQueue(vehicle);
            }

        } else {
            vehicle.arrivedInQueue(stk.getCurrentTime());
            stk.queueBeforeStk.addQueue(vehicle);
        }

//        // ak niekto caka na platbu a je volny zamestanec zo skupiny 1
//        if (stk.queueAfterStk.getSize() > 0 && stk.group1.isWorkerFree()) {
//            final Vehicle newVehicle = stk.queueAfterStk.poll();
//            stk.group1.hireWorker(newVehicle);
//            stk.scheduleStartPayment(newVehicle);
//            // ak niekto caka pred stk a je volny zamestnace zo skupiny 1 a je volne miesto na parkovisku
//        } else if (
//                stk.queueBeforeStk.getSize() > 0 &&
//                        stk.isSpaceInsideStk() &&
//                        stk.group1.isWorkerFree()
//        ) {
//            final Vehicle newVehicle = stk.queueBeforeStk.poll();
//            stk.group1.hireWorker(newVehicle);
//            stk.arrivedInStkQueue(newVehicle);
//            stk.scheduleReceiveVehicle(newVehicle);
//        } else {
//            vehicle.arrivedInQueue(stk.getCurrentTime());
//            stk.queueBeforeStk.addQueue(vehicle);
//        }


//        // ak je volny woker a je miesto v rade na kontrolu
//        if (stk.group1.isWorkerFree() && stk.isSpaceInsideStk()) {
//            vehicle.startWaitingInQue = stk.getCurrentTime();
//            stk.averageWaitingBeforeSTK.countAverageTimeInQueue(vehicle.startWaitingInQue);
//            stk.group1.hireWorker(this.vehicle);
//            stk.arrivedInStkQueue(this.vehicle);
//            stk.scheduleReceiveVehicle(this.vehicle);
//            stk.averageQueueBeforeSTK.countAverageQueueLength();
//        // vojdeme do radu pred stk
//        } else {
//            vehicle.arrivedInQueue(stk.getCurrentTime());
//            stk.queueBeforeStk.addQueue(vehicle);
//        }
    }

    public VehicleArrivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
