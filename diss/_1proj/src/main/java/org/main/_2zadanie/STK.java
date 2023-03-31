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
    final Queue<Integer, Vehicle> queueBeforeStk = new Queue<>();
    final Queue<Long, Vehicle> queueInStk = new Queue<>(5);
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
    public final AverageQueueLength averageQueueBeforeSTK = new AverageQueueLength(this, queueBeforeStk.getQueue());


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
        queueBeforeStk.assignStatistics(averageQueueBeforeSTK);

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
        queueBeforeStk.assignStatistics(averageQueueBeforeSTK);
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
        averageQueueBeforeSTK.initialize();

        queueBeforeStk.clear();
        queueInStk.clear();
        queueAfterStk.clear();

        group1.clear();
        group2.clear();

        arrivedVehicles.clear();
        leftVehicles.clear();

        vehicleId = 0;
    }
    @Override
    protected void beforeSimulation() { }

    @Override
    protected void afterSimulation() {
        System.out.println("replikacie:                             " + replications);
        System.out.println("pracovnikov 1:                          " + group1.getNumberOfWorkers());
        System.out.println("pracovnikov 2:                          " + group2.getNumberOfWorkers());
        System.out.println("vozidla v stk po ukonceni:              " + averageVehiclesInSTK.totalResult());
        System.out.println("priemerny cas vozidla v stk:            " + averageVehicleTimeInSystem.totalResult()/60 + " min");
        System.out.println("priemerny pocet volnych pracovnikov 1   " + averageFreeWorker1.totalResult());
        System.out.println("priemerny pocet volnych pracovnikov 2   " + averageFreeWorker2.totalResult());
        System.out.println("priemerna dlzka rady pred stk           " + averageQueueBeforeSTK.totalResult());
    }

    @Override
    protected void beforeReplication() {
        initialize();
        scheduleNewArrival();
    }

    @Override
    protected void afterReplication() {
        averageVehiclesInSTK.countResult();
        averageVehicleTimeInSystem.countResult();
        averageFreeWorker1.countResult();
        averageFreeWorker2.countResult();
        averageQueueBeforeSTK.countResult();
        initialize();
    }
}
