package shared.Workers;

public class Worker {
    private WorkersGroup workersGroup;
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
}
