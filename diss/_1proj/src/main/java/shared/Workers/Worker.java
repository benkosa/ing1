package shared.Workers;

public class Worker {
    final private int id;
    private boolean hadLunchBreak = false;

    public int getId() {
        return id;
    }

    public Worker(int id) {
        this.id = id;
    }

    public void hadLunchBreak() {
        hadLunchBreak = true;
    }
}
