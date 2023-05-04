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

    public boolean isLunchBreakTime() {
        return lunchBreakStarted;
    }

    public void startLunchBreak() {
        lunchBreakStarted = true;
        while (workers.size() > 0) {
            countAverageFreeWorker();
            _startLunchBreak(workers.poll());
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
    private void _startLunchBreak(Worker worker) {
        worker.hadLunchBreak();
        lunchBreakWorkers.put(worker.getId(), worker);
    }

    public void startLunchBreak(Worker worker) {
        if (!workers.remove(worker)) {
            System.out.println("warning remove not existing worker");
        }
        worker.hadLunchBreak();
        lunchBreakWorkers.put(worker.getId(), worker);
    }



}
