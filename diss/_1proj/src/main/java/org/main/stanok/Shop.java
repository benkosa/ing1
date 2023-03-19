package org.main.stanok;

import org.main.shared.Distribution.ExponentialDistribution;
import org.main.shared.EventSimulation.EventSimulationCore;

import java.util.PriorityQueue;
import java.util.Random;

public class Shop extends EventSimulationCore {

    Random genSeed;

    ExponentialDistribution customerArrived;
    ExponentialDistribution customerServing;

    protected boolean isServing = false;
    final PriorityQueue<Customer> shopQueue = new PriorityQueue<>();

    public Shop(long replications, long maxTime, int seed) {
        super(replications, maxTime);
        genSeed = new Random(seed);
        customerArrived = new ExponentialDistribution(genSeed, 5);
        customerServing = new ExponentialDistribution(genSeed, 4);
    }

    @Override
    protected void beforeSimulation() { }

    @Override
    protected void afterSimulation() { }

    @Override
    protected void beforeReplication() {
        initialize();
        scheduleNewArrival();
    }

    @Override
    protected void afterReplication() {
        System.out.println(countEventsInQueue/lastTimeChange);
        System.out.println(countTimeInQueue/countCustomersInQueue);
        initialize();
    }

    public void scheduleNewArrival() {
        addEvent(new CustomerArrivedEvent(customerArrived.sample(), this, new Customer(0)));
    }

    private double lastTimeChange = 0;
    private double countEventsInQueue = 0;

    public void countAverageQueueSize(int queueSize) {
        final double time = this.getCurrentTime() - lastTimeChange;
        countEventsInQueue += time*queueSize;
        lastTimeChange = this.getCurrentTime();
    }

    private double countTimeInQueue = 0;
    private long countCustomersInQueue = 0;
    public void countAverageTimeInQueue(double startWaitingInQue) {
        countTimeInQueue+=getCurrentTime()-startWaitingInQue;
        countCustomersInQueue+=1;
    }

    private void initialize() {
        lastTimeChange = 0;
        countEventsInQueue = 0;
        countTimeInQueue = 0;
        countCustomersInQueue = 0;
        isServing = false;
        shopQueue.clear();
    }
}
