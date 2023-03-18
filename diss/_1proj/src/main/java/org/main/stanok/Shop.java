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
        while (shopQueue.size() != 0) {
            shopQueue.poll();
            countAverageQueueSize(shopQueue.size());
        }
        System.out.println(countEventsInQueue/this.getCurrentTime());
        System.out.println(countTimeInQueue/countCustomersInQueue);
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
}
