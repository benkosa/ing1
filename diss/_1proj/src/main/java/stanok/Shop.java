package stanok;

import shared.Distribution.ExponentialDistribution;
import shared.Distribution.SeedGenerator;
import shared.EventSimulation.EventSimulationCore;
import shared.Statistics.AverageQueueLength;
import shared.Statistics.AverageWaitingTimeInQueue;
import shared.Statistics.Core;

import java.util.PriorityQueue;

public class Shop extends EventSimulationCore implements Core {

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
