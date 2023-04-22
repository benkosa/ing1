package shared.Statistics;

public abstract class Statistics {
    private double countReplications = 0;
    private double sumResults = 0;

    public double getCountReplications() {
        return countReplications;
    }

    protected abstract double replicationResult();
    public double totalResult() {
        return sumResults / countReplications;
    }
    private void addStatistics(double result) {
        sumResults+= result;
        countReplications+=1;
    }
    public double countResult() {
        double result = replicationResult();
        addStatistics(result);
        return result;
    }
    public abstract void initialize();
}
