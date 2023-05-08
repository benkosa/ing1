package shared.Workers;

import shared.Statistics.AverageQueueLength;
import shared.Statistics.Core;

import java.util.HashMap;
import java.util.LinkedList;

public class WorkersGroup<T extends Groupable> {
    final protected LinkedList<Worker> workers = new LinkedList<>();

    protected Core core;

    public boolean isExpensive() {
        return isExpensive;
    }

    public void setExpensive(boolean expensive) {
        isExpensive = expensive;
    }

    private boolean isExpensive;
    public LinkedList<Worker> getWorkers() {
        return workers;
    }
    public HashMap<Long, Worker> getHiredWorkers() {
        return hiredWorkers;
    }
    final protected HashMap<Long, Worker> hiredWorkers = new HashMap<>();
    protected AverageQueueLength averageFreeWorker;
    final private int numberOfWorkers;
    public int getNumberOfWorkers() {
        return workers.size() + hiredWorkers.size();
    }
    public int getWorkersInUsage() {
        return hiredWorkers.size();
    }
    public WorkersGroup(int numberOfWorkers, Core core) {
        this.core = core;
        this.numberOfWorkers = numberOfWorkers;
        addWorkers();
    }

    protected void addWorkers() {
        for (int i = 0; i < numberOfWorkers; i++) {
            workers.push(new Worker(i, this));
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
        worker.startedWork = core.getCurrentTime();
        hiredWorkers.put(vehicle.getId(), worker);
    }

    protected void countAverageFreeWorker() {
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
