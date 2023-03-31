package org.main._2zadanie;

import org.main._2zadanie.Workers.WorkersGroup;
import org.main.shared.Distribution.*;
import org.main.shared.EventSimulation.EventSimulationCore;
import org.main.shared.EventSimulation.Queue;
import org.main.shared.Statistics.AverageQueueLength;
import org.main.shared.Statistics.AverageVehicleTimeInSystem;
import org.main.shared.Statistics.AverageVehiclesInSTK;

import java.util.LinkedList;

public class STK extends EventSimulationCore {
    final SeedGenerator seedGenerator;
    final ExponentialDistribution VehicleArrived;
    final UniformDouble vehicleTypeGen;
    final DiscreteUniformDistribution paymentTime;
    final TriangularDistribution triangularDistribution;
    //final PriorityQueue<Vehicle> queueBeforeStk = new PriorityQueue<>();
    final Queue<Integer, Vehicle> queueBeforeStk = new Queue<>();
    final int queueInStkCapacity = 5;
    //final PriorityQueue<Vehicle> queueInStk = new PriorityQueue<>();
    final Queue<Long, Vehicle> queueInStk = new Queue<>(5);
    //final PriorityQueue<Vehicle> queueAfterStk = new PriorityQueue<>();
    final Queue<Integer, Vehicle> queueAfterStk = new Queue<>();
    WorkersGroup group1;
    WorkersGroup group2;
    private long vehicleId = 0;
    LinkedList<Vehicle> arrivedVehicles = new LinkedList<>();
    LinkedList<Vehicle> leftVehicles = new LinkedList<>();

    public final AverageVehiclesInSTK averageVehiclesInSTK = new AverageVehiclesInSTK();
    public final AverageVehicleTimeInSystem averageVehicleTimeInSystem = new AverageVehicleTimeInSystem(this);
    public final AverageQueueLength averageFreeWorker1;
    public final AverageQueueLength averageFreeWorker2;


    public STK(long replications, long maxTime, int seed, int workers1, int workers2) {
        super(replications, maxTime);
        seedGenerator = new SeedGenerator(seed);
        vehicleTypeGen = new UniformDouble(seedGenerator);
        VehicleArrived = new ExponentialDistribution(seedGenerator, (double)(60/23)*60);
        paymentTime = new DiscreteUniformDistribution(seedGenerator, 65, 177);
        triangularDistribution = new TriangularDistribution(seedGenerator, 180, 695, 431);
        setWorkers(workers1, workers2);
        averageFreeWorker1 = new AverageQueueLength(this, group1.getWorkers());
        averageFreeWorker2 = new AverageQueueLength(this, group2.getWorkers());
        workersAssignStatistics();

    }

    public STK(long replications, long maxTime, int seed, int workers1, int workers2, int stepLength, int stepTime) {
        super(replications, maxTime);
        seedGenerator = new SeedGenerator(seed);
        vehicleTypeGen = new UniformDouble(seedGenerator);
        VehicleArrived = new ExponentialDistribution(seedGenerator, (double)(60/23)*60);
        paymentTime = new DiscreteUniformDistribution(seedGenerator, 65, 177);
        triangularDistribution = new TriangularDistribution(seedGenerator, 180, 695, 431);
        setWorkers(workers1, workers2);
        changeSlowDown(stepLength, stepTime);
        averageFreeWorker1 = new AverageQueueLength(this, group1.getWorkers());
        averageFreeWorker2 = new AverageQueueLength(this, group2.getWorkers());
        workersAssignStatistics();
    }


    public void changeSlowDown(int stepLength, int stepTime) {
        this.stepLength = stepLength;
        this.sleepTime = stepTime;
    }

    public void setWorkers(int g1, int g2) {
        group1 = new WorkersGroup(g1);
        group2 = new WorkersGroup(g2);
    }

    public void workersAssignStatistics() {
        group1.assignStatistics(averageFreeWorker1);
        group2.assignStatistics(averageFreeWorker2);
    }

    public void scheduleNewArrival() {
        //24300 == (6*60+45)*60
        if (getCurrentTime() < 24300) {
            final double arrivedTime = VehicleArrived.sample();
            if ((getCurrentTime() + arrivedTime) < 24300) {
                averageVehiclesInSTK.vehicleArrived();
                addEvent(
                        new VehicleArrivedEvent(
                                VehicleArrived.sample(),
                                this,
                                new Vehicle(seedGenerator, vehicleTypeGen.sample(), vehicleId += 1)
                        )
                );
            }
        }
    }
    public void scheduleReceiveVehicle(Vehicle vehicle){
        addEvent(new VehicleReceivedEvent(0, this, vehicle));
    }
    public boolean isSpaceInsideStk() {
        return queueInStk.isSpaceInQueue();
    }
    public boolean isWaitingCarForInspection() {
        return queueInStk.getReadySize() > 0;
    }
    public void arrivedInStkQueue(Vehicle vehicle) {
        this.queueInStk.addQueueLocked(vehicle.id, vehicle);
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
    protected void saveArrivedVehicle(Vehicle vehicle) {
        if (isLiveMode()) {
            arrivedVehicles.add(vehicle);
        }
    }
    protected void saveLeftVehicle(Vehicle vehicle) {
        if (isLiveMode()) {
            leftVehicles.add(vehicle);
        }
    }
    private void initialize() {
        averageVehiclesInSTK.initialize();
        averageVehicleTimeInSystem.initialize();
        averageFreeWorker1.initialize();
        averageFreeWorker2.initialize();
    }

    @Override
    protected void beforeSimulation() {
        initialize();
        scheduleNewArrival();
    }

    @Override
    protected void afterSimulation() {
        System.out.println(averageVehiclesInSTK.totalResult());
        System.out.println(averageVehicleTimeInSystem.totalResult());
        System.out.println(averageFreeWorker1.totalResult());
        System.out.println(averageFreeWorker2.totalResult());
    }

    @Override
    protected void beforeReplication() {

    }

    @Override
    protected void afterReplication() {
        averageVehiclesInSTK.countResult();
        averageVehicleTimeInSystem.countResult();
        averageFreeWorker1.countResult();
        averageFreeWorker2.countResult();

    }
}
