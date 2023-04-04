package org.main._2zadanie;

import org.main._2zadanie.Workers.WorkersGroup;
import org.main.shared.Distribution.*;
import org.main.shared.EventSimulation.EventSimulationCore;
import org.main.shared.EventSimulation.Queue;
import org.main.shared.Statistics.AverageQueueLength;
import org.main.shared.Statistics.AverageVehicleTimeInSystem;
import org.main.shared.Statistics.AverageVehiclesInSTK;
import org.main.shared.Statistics.AverageWaitingTimeInQueue;

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
    public final AverageWaitingTimeInQueue averageWaitingBeforeSTK = new AverageWaitingTimeInQueue(this);
    private final Queue<Long, Vehicle> queueInSystem = new Queue<>();
    public final AverageQueueLength averageQueueInSystem = new AverageQueueLength(this, queueInSystem.getLockedQueue());
    public final AverageQueueLength averageQueueBeforeSTK = new AverageQueueLength(this, queueBeforeStk.getQueue());


    public STK(long replications, long maxTime, int seed, int workers1, int workers2) {
        super(replications, maxTime);
        seedGenerator = new SeedGenerator(seed);
        vehicleTypeGen = new UniformDouble(seedGenerator);
        VehicleArrived = new ExponentialDistribution(seedGenerator, (60.0/23.0)*60);
        paymentTime = new DiscreteUniformDistribution(seedGenerator, 65, 177);
        triangularDistribution = new TriangularDistribution(seedGenerator, 180, 695, 431);
        setWorkers(workers1, workers2);
        averageFreeWorker1 = new AverageQueueLength(this, group1.getWorkers());
        averageFreeWorker2 = new AverageQueueLength(this, group2.getWorkers());
        workersAssignStatistics();
        queueBeforeStk.assignStatistics(averageWaitingBeforeSTK);
        queueBeforeStk.assignStatisticsQueueLength(averageQueueBeforeSTK);

        inspectionTimePersonal = new DiscreteUniformDistribution(seedGenerator, 31*60, 45*60);
        inspectionTimeVan = new DiscreteEmpiricalDistribution(
                new int[]{35*60, 38*60, 41*60, 48*60},
                new int[]{37*60, 40*60, 47*60, 52*60},
                new double[]{.2, .35, .3, .15},
                seedGenerator
        );
        inspectionTimeCargo = new DiscreteEmpiricalDistribution(
                new int[]{37*60, 43*60, 46*60, 48 ,52*60, 56*60},
                new int[]{42*60, 45*60, 47*60, 51*60, 55*60, 65*60},
                new double[]{.05, .1, .15, .4, .25, .05},
                seedGenerator
        );


    }

    public STK(long replications, long maxTime, int seed, int workers1, int workers2, int stepLength, int stepTime) {
        super(replications, maxTime);
        seedGenerator = new SeedGenerator(seed);
        vehicleTypeGen = new UniformDouble(seedGenerator);
        VehicleArrived = new ExponentialDistribution(seedGenerator, (60.0/23.0)*60);
        paymentTime = new DiscreteUniformDistribution(seedGenerator, 65, 177);
        triangularDistribution = new TriangularDistribution(seedGenerator, 180, 695, 431);
        setWorkers(workers1, workers2);
        changeSlowDown(stepLength, stepTime);
        averageFreeWorker1 = new AverageQueueLength(this, group1.getWorkers());
        averageFreeWorker2 = new AverageQueueLength(this, group2.getWorkers());
        workersAssignStatistics();
        queueBeforeStk.assignStatistics(averageWaitingBeforeSTK);
        queueBeforeStk.assignStatisticsQueueLength(averageQueueBeforeSTK);

        inspectionTimePersonal = new DiscreteUniformDistribution(seedGenerator, 31*60, 45*60);
        inspectionTimeVan = new DiscreteEmpiricalDistribution(
                new int[]{35*60, 38*60, 41*60, 48*60},
                new int[]{37*60, 40*60, 47*60, 52*60},
                new double[]{.2, .35, .3, .15},
                seedGenerator
        );
        inspectionTimeCargo = new DiscreteEmpiricalDistribution(
                new int[]{37*60, 43*60, 46*60, 48 ,52*60, 56*60},
                new int[]{42*60, 45*60, 47*60, 51*60, 55*60, 65*60},
                new double[]{.05, .1, .15, .4, .25, .05},
                seedGenerator
        );
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
        //if (getCurrentTime() < 24300) {
            final double arrivedTime = VehicleArrived.sample();
            if ((getCurrentTime() + arrivedTime) <= 24300) {
                averageVehiclesInSTK.vehicleArrived();
                addEvent(
                        new VehicleArrivedEvent(
                                arrivedTime,
                                this,
                                generateVehicle()
                        )
                );
            }
       // }
    }
    public void scheduleNewArrival(int time) {
        averageVehiclesInSTK.vehicleArrived();
        addEvent(
                new VehicleArrivedEvent(
                        time,
                        this,
                        generateVehicle()
                )
        );
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
        vehicle.startWaitingInQue = getCurrentTime();
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
        queueInSystem.addQueueLocked(vehicle.id, vehicle);
        averageQueueInSystem.countAverageQueueLength();
        if (isLiveMode()) {
            arrivedVehicles.add(vehicle);
        }
    }
    protected void saveLeftVehicle(Vehicle vehicle) {
        queueInSystem.move(vehicle.id);
        averageQueueInSystem.countAverageQueueLength();
        if (isLiveMode()) {
            leftVehicles.add(vehicle);
        }
    }
    private void initialize() {
        averageVehiclesInSTK.initialize();
        averageVehicleTimeInSystem.initialize();
        averageFreeWorker1.initialize();
        averageFreeWorker2.initialize();
        averageWaitingBeforeSTK.initialize();
        averageQueueInSystem.initialize();
        averageQueueBeforeSTK.initialize();

        queueBeforeStk.clear();
        queueInStk.clear();
        queueAfterStk.clear();
        queueInSystem.clear();

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
        forceRefresh("result");
    }

    @Override
    protected void beforeReplication() {
        initialize();
        scheduleNewArrival(0);
    }

    @Override
    protected void afterReplication() {
        averageVehiclesInSTK.countResult();
        averageVehicleTimeInSystem.countResult();
        averageFreeWorker1.countResult();
        averageFreeWorker2.countResult();
        averageWaitingBeforeSTK.countResult();
        averageQueueInSystem.countResult();
        averageQueueBeforeSTK.countResult();
        initialize();
    }

    private final DiscreteUniformDistribution inspectionTimePersonal;
    private final DiscreteEmpiricalDistribution inspectionTimeVan;
    private final DiscreteEmpiricalDistribution inspectionTimeCargo;
    private Vehicle generateVehicle() {
        double p = vehicleTypeGen.sample();
        double inspectionTime;
        VehicleType vehicleType;
        if (p < .65) {
            inspectionTime = inspectionTimePersonal.sample();
            vehicleType = VehicleType.PERSONAL;
        } else if (p < (.65+.21)) {
            inspectionTime = inspectionTimeVan.sample();
            vehicleType = VehicleType.VAN;
        } else {
            inspectionTime = inspectionTimeCargo.sample();
            vehicleType = VehicleType.CARGO;
        }

        return new Vehicle(vehicleType, inspectionTime, vehicleId += 1);
    }
}
