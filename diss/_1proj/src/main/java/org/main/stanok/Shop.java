package org.main.stanok;

import org.main.shared.Distribution.ExponentialDistribution;
import org.main.shared.EventSimulation.EventSimulationCore;

import java.util.PriorityQueue;
import java.util.Random;

public class Shop extends EventSimulationCore {

    Random genSeed = new Random();

    ExponentialDistribution customerArrived = new ExponentialDistribution(genSeed, 5);
    ExponentialDistribution customerServing = new ExponentialDistribution(genSeed, 4);

    protected boolean isServing = false;
    final PriorityQueue<Customer> shopQueue = new PriorityQueue<>();

    public Shop(long replications, long maxTime) {
        super(replications, maxTime);
    }

    @Override
    protected void beforeSimulation() {
        scheduleNewArrival();
    }

    @Override
    protected void afterSimulation() {
        System.out.println((double) countEventsInQueue/this.getCurrentTime());
        System.out.println(countTimeInQueue/countCustomersInQueue);
    }

    public void scheduleNewArrival() {
        addEvent(new CustomerArrivedEvent(customerArrived.sample(), this, new Customer(0)));
    }

    double lastTimeChange = 0;
    public long countEventsInQueue = 0;

    public void countAverageQueueSize(int queueSize) {
        final double time = this.getCurrentTime() - lastTimeChange;
        this.countEventsInQueue += time*queueSize;
        this.lastTimeChange = this.getCurrentTime();
    }

    double countTimeInQueue = 0;
    long countCustomersInQueue = 0;
    public void countAverageTimeInQueue(double startWaitingInQue) {
        countTimeInQueue+=getCurrentTime()-startWaitingInQue;
        countCustomersInQueue+=1;
    }
}
