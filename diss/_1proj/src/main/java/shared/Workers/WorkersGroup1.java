package shared.Workers;

import java.util.HashMap;

public class WorkersGroup1<T extends Groupable> extends WorkersGroup<T>{
    public WorkersGroup1(int numberOfWorkers) {
        super(numberOfWorkers);
    }

    final private HashMap<Integer, Worker> lunchBreakWorkers = new HashMap<>();

    public HashMap<Integer, Worker> getLunchBreakWorkers() {
        return lunchBreakWorkers;
    }

    private boolean lunchBreakStarted = false;

    public void startLunchBreak() {
        lunchBreakStarted = true;
        while (workers.size() > 0) {
            countAverageFreeWorker();
            startLunchBreak(workers.poll());
        }
    }

    public int getNumberOfWorkers() {
        return workers.size() + hiredWorkers.size() + lunchBreakWorkers.size();
    }

    public void endLunchBreakWorker(Worker worker) {
        lunchBreakWorkers.remove(worker.getId());
        countAverageFreeWorker();
        workers.add(worker);
    };
    public void startLunchBreak(Worker worker) {
        worker.hadLunchBreak();
        lunchBreakWorkers.put(worker.getId(), worker);
    }



}
