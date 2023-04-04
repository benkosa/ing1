package org.main.stanok;

import org.main.shared.Distribution.ExponentialDistribution;
import org.main.shared.Distribution.SeedGenerator;
import org.main.shared.EventSimulation.EventSimulationCore;
import org.main.shared.Statistics.AverageQueueLength;
import org.main.shared.Statistics.AverageWaitingTimeInQueue;

import java.util.PriorityQueue;
import java.util.Random;

public class Shop extends EventSimulationCore {

    SeedGenerator genSeed;

    ExponentialDistribution customerArrived;
    ExponentialDistribution customerServing;

    public AverageQueueLength averageQueueLength;
    public AverageWaitingTimeInQueue averageWaitingTimeInQueue;

    protected boolean isServing = false;
    final PriorityQueue<Customer> shopQueue = new PriorityQueue<>();

    public Shop(long replications, long maxTime, int seed) {
        super(replications, maxTime);
        genSeed = new SeedGenerator(seed);
        customerArrived = new ExponentialDistribution(genSeed, 5);
        customerServing = new ExponentialDistribution(genSeed, 4);
        averageQueueLength = new AverageQueueLength(this, shopQueue);
        averageWaitingTimeInQueue = new AverageWaitingTimeInQueue(this);
    }
    private void initialize() {
        averageQueueLength.initialize();
        averageWaitingTimeInQueue.initialize();
        isServing = false;
        shopQueue.clear();
    }

    @Override
    protected void beforeSimulation() { }

    @Override
    protected void afterSimulation() {
        System.out.println(averageWaitingTimeInQueue.totalResult());
        System.out.println(averageQueueLength.totalResult());
    }

    @Override
    protected void beforeReplication() {
        System.out.println("bol som tu");
        initialize();
        scheduleNewArrival();
    }

    @Override
    protected void afterReplication() {
        averageQueueLength.countResult();
        averageWaitingTimeInQueue.countResult();
        initialize();
    }

    public void scheduleNewArrival() {
        addEvent(new CustomerArrivedEvent(customerArrived.sample(), this, new Customer(0)));
    }



    public void countAverageQueueSize() {
        averageQueueLength.countAverageQueueLength();
    }

}
