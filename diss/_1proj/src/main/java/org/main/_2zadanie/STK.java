package org.main._2zadanie;

import org.main._2zadanie.Workers.Group1;
import org.main._2zadanie.Workers.WorkersGroup;
import org.main.shared.Distribution.ExponentialDistribution;
import org.main.shared.Distribution.SeedGenerator;
import org.main.shared.Distribution.UniformDouble;
import org.main.shared.EventSimulation.EventSimulationCore;
import org.main.stanok.Customer;
import org.main.stanok.CustomerArrivedEvent;

import java.util.PriorityQueue;

public class STK extends EventSimulationCore {

    SeedGenerator seedGenerator;
    ExponentialDistribution VehicleArrived;
    UniformDouble vehicleTypeGen;
    final PriorityQueue<Vehicle> queueBeforeStk = new PriorityQueue<>();
    final PriorityQueue<Vehicle> queueInStk = new PriorityQueue<>();
    final PriorityQueue<Vehicle> queueAfterStk = new PriorityQueue<>();
    final WorkersGroup group1;


    public STK(long replications, long maxTime, int seed) {
        super(replications, maxTime);
        seedGenerator = new SeedGenerator(seed);
        vehicleTypeGen = new UniformDouble(seedGenerator);
        VehicleArrived = new ExponentialDistribution(seedGenerator, (double)60/23);
        group1 = new Group1(5, this);
    }

    public void scheduleNewArrival() {
        addEvent(
                new VehicleArrivedEvent(
                        VehicleArrived.sample(),
                        this,
                        new Vehicle(seedGenerator, vehicleTypeGen.sample())
                )
        );
    }

    private void initialize() {
    }

    @Override
    protected void beforeSimulation() {
        initialize();
        scheduleNewArrival();
    }

    @Override
    protected void afterSimulation() {

    }

    @Override
    protected void beforeReplication() {

    }

    @Override
    protected void afterReplication() {

    }


}
