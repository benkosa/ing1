package org.main.shared.Statistics;

public abstract class Statistics {
    private long countReplications = 0;
    private double sumResults = 0;
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
