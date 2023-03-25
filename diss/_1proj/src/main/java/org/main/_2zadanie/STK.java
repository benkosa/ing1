package org.main._2zadanie;

import org.main._2zadanie.Workers.Group1;
import org.main._2zadanie.Workers.Group2;
import org.main._2zadanie.Workers.WorkersGroup;
import org.main.shared.Distribution.*;
import org.main.shared.EventSimulation.EventSimulationCore;

import java.util.PriorityQueue;

public class STK extends EventSimulationCore {

    final SeedGenerator seedGenerator;
    final ExponentialDistribution VehicleArrived;
    final UniformDouble vehicleTypeGen;
    final DiscreteUniformDistribution paymentTime;
    final TriangularDistribution triangularDistribution;
    final PriorityQueue<Vehicle> queueBeforeStk = new PriorityQueue<>();
    final int queueInStkCapacity = 5;
    final PriorityQueue<Vehicle> queueInStk = new PriorityQueue<>();
    final PriorityQueue<Vehicle> queueAfterStk = new PriorityQueue<>();
    final WorkersGroup group1;
    final WorkersGroup group2;


    public STK(long replications, long maxTime, int seed) {
        super(replications, maxTime);
        seedGenerator = new SeedGenerator(seed);
        vehicleTypeGen = new UniformDouble(seedGenerator);
        VehicleArrived = new ExponentialDistribution(seedGenerator, (double)(60/23)*60);
        paymentTime = new DiscreteUniformDistribution(seedGenerator, 65, 177);
        triangularDistribution = new TriangularDistribution(seedGenerator, 180, 695, 431);
        group1 = new Group1(5, this);
        group2 = new Group2(5, this);
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
    public void scheduleReceiveVehicle(Vehicle vehicle){
        addEvent(new VehicleReceivedEvent(0, this, vehicle));
    }

    public boolean isSpaceInsideStk() {
        return queueInStk.size() < queueInStkCapacity;
    }
    public boolean isWaitingCarForInspection() {
        return queueInStk.size() > 0;
    }

    public void arrivedInStkQueue(Vehicle vehicle) {
        this.queueInStk.add(vehicle);
        if (queueInStk.size() > queueInStkCapacity) {
            System.out.println("error: arrivedInStkQueue queueInStk > queueInStkCapacity");
        }
    }

    public void scheduleReceiveVehicleEnd(Vehicle vehicle) {
        addEvent(new VehicleReceivedEndEvent(triangularDistribution.sample(), this, vehicle));
    }

    public void scheduleStartPayment(Vehicle vehicle) {
        addEvent(new VehiclePaymentEvent(0, this, vehicle));
    }
    public void scheduleEndPayment(Vehicle vehicle) {
        addEvent(new VehiclePaymentEndEvent(paymentTime.sample(), this, vehicle));
    }

    public void scheduleStartInspection(Vehicle vehicle) {
        addEvent(new VehicleInspectionEvent(0, this, vehicle));
    }

    public void scheduleEndInspection(Vehicle vehicle) {
        addEvent(new VehicleInspectionEndEvent(vehicle.getInspectionTime(), this, vehicle));
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
