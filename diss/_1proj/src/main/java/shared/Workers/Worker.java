package shared.Workers;

import _3zadanie.simulation.MyMessage;
import shared.Statistics.Core;

public class Worker {
    public WorkersGroup workersGroup;
    protected double startedWork = 0;

    public double getStartedWork() {
        return startedWork;
    }

    final private int id;

    public boolean isHadLunchBreak() {
        return hadLunchBreak;
    }

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
        WorkersGroupLunchBreak workersGroup1 = (WorkersGroupLunchBreak) workersGroup;
        workersGroup1.endLunchBreakWorker(this);
    }

    public void freeWorker(MyMessage message) {
        workersGroup.freeWorker(message);
    }

    public void hireWorker(MyMessage message) {
        startedWork = workersGroup.core.getCurrentTime();
        workersGroup.hireWorker(message);
    }

    public boolean isExpensive() {
        return workersGroup.isExpensive();
    }
}
