package _2zadanie.Workers;

import _2zadanie.Vehicle;
import shared.Statistics.AverageQueueLength;

import java.util.HashMap;
import java.util.LinkedList;

public class WorkersGroup<T extends Groupable> {
    final private LinkedList<Worker> workers = new LinkedList<>();
    public LinkedList<Worker> getWorkers() {
        return workers;
    }
    public HashMap<Long, Worker> getHiredWorkers() {
        return hiredWorkers;
    }
    final private HashMap<Long, Worker> hiredWorkers = new HashMap<>();
    protected AverageQueueLength averageFreeWorker;
    final private int numberOfWorkers;
    public int getNumberOfWorkers() {
        return workers.size() + hiredWorkers.size();
    }
    public int getWorkersInUsage() {
        return hiredWorkers.size();
    }
    public WorkersGroup(int numberOfWorkers) {
        this.numberOfWorkers = numberOfWorkers;
        addWorkers();
    }

    private void addWorkers() {
        for (int i = 0; i < numberOfWorkers; i++) {
            workers.push(new Worker(i));
        }
    }

    /** */
    public void freeWorker(T vehicle) {
        countAverageFreeWorker();
        final Worker worker = hiredWorkers.get(vehicle.getId());
        if (worker == null) {
            System.out.println("warning: free not existing worker");
            return;
        }
        hiredWorkers.remove(vehicle.getId());
        workers.add(worker);
    };
    public boolean isWorkerFree() {
        return workers.size() > 0;
    }
    public void hireWorker(T vehicle) {
        countAverageFreeWorker();
        final Worker worker = workers.poll();
        if (worker == null) {
            System.out.println("warning: hire not existing worker");
            return;
        }
        hiredWorkers.put(vehicle.getId(), worker);
    }

    private void countAverageFreeWorker() {
        if (averageFreeWorker != null)
            averageFreeWorker.countAverageQueueLength();
    }


    public void assignStatistics(AverageQueueLength averageFreeWorker) {
        this.averageFreeWorker = averageFreeWorker;
    }

    public void clear() {
        workers.clear();
        hiredWorkers.clear();
        addWorkers();
    }
}
