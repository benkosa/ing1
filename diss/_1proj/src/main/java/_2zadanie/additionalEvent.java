package _2zadanie;

import shared.EventSimulation.EventSimulationCore;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class additionalEvent extends VehicleEvent {
    @Override
    public void execute() {
        PriorityQueue<Vehicle> queue = (PriorityQueue<Vehicle>)stk.queueBeforeStk.getQueue();
        LinkedList<Vehicle> elementsToRemove = new LinkedList<>();
        queue.forEach(element -> {
            double waitTime = stk.getCurrentTime() - element.startWaitingInQue;
            if(waitTime > 180) {
                elementsToRemove.add(element);
            }
        });

        elementsToRemove.forEach(element -> {
            double waitTime = stk.getCurrentTime() - element.startWaitingInQue;
            System.out.println("remove " + element.id + " " + waitTime/60);
            queue.remove(element);
        });
    }

    public additionalEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
