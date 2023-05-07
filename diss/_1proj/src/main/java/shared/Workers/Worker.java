package shared.Workers;

import _3zadanie.simulation.MyMessage;

public class Worker {
    public WorkersGroup workersGroup;
    final private int id;
    private boolean hadLunchBreak = false;

    public int getId() {
        return id;
    }

    public Worker(int id, WorkersGroup workersGroup) {
        this.id = id;
        this.workersGroup = workersGroup;
    }

    public void hadLunchBreak() {
        hadLunchBreak = true;
    }

    public boolean shouldGoToLunchBreak() {
        return !hadLunchBreak;
    }

    public void endLunchBreak() {
        WorkersGroup1 workersGroup1 = (WorkersGroup1) workersGroup;
        workersGroup1.endLunchBreakWorker(this);
    }

    public void freeWorker(MyMessage message) {
        workersGroup.freeWorker(message);
    }

    public void hireWorker(MyMessage message) {
        workersGroup.hireWorker(message);
    }

    public boolean isExpensive() {
        return workersGroup.isExpensive();
    }
}
