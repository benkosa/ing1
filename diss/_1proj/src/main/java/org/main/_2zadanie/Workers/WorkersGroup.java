package org.main._2zadanie.Workers;

import org.main._2zadanie.Vehicle;

import java.util.HashMap;
import java.util.LinkedList;

public class WorkersGroup {
    final private LinkedList<Worker> workers = new LinkedList<>();

    public LinkedList<Worker> getWorkers() {
        return workers;
    }

    public HashMap<Long, Worker> getHiredWorkers() {
        return hiredWorkers;
    }

    final private HashMap<Long, Worker> hiredWorkers = new HashMap<>();

    public int getNumberOfWorkers() {
        return workers.size() + hiredWorkers.size();
    }
    public int getWorkersInUsage() {
        return hiredWorkers.size();
    }
    public WorkersGroup(int numberOfWorkers) {
        for (int i = 0; i < numberOfWorkers; i++) {
            workers.push(new Worker(i));
        }
    }
    /**
     *
     */
    public void freeWorker(Vehicle vehicle) {
        final Worker worker = hiredWorkers.get(vehicle.id);
        if (worker == null) {
            System.out.println("warning: free not existing worker");
            return;
        }
        hiredWorkers.remove(vehicle.id);
        workers.add(worker);

    };
    public boolean isWorkerFree() {
        return workers.size() > 0;
    }
    public void hireWorker(Vehicle vehicle) {
        final Worker worker = workers.poll();
        if (worker == null) {
            System.out.println("warning: hire not existing worker");
            return;
        }
        hiredWorkers.put(vehicle.id, worker);
    }




}
