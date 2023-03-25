package org.main._2zadanie;

import org.main.shared.Distribution.BasicDistribution;
import org.main.shared.Distribution.DiscreteEmpiricalDistribution;
import org.main.shared.Distribution.DiscreteUniformDistribution;
import org.main.shared.Distribution.SeedGenerator;
import org.main.stanok.Customer;

public class Vehicle implements Comparable<Vehicle>{
    private double inspectionTime;
    SeedGenerator seedGenerator;
    protected double startWaitingInQue;
    protected boolean isInFifoQueue;
    private VehicleType vehicleType;

    public Vehicle(SeedGenerator seedGenerator, double type) {
        this.seedGenerator = seedGenerator;
        choseType(type);
    }

    private void choseType(double p) {
        if (p < .14) {
            inspectionTime =  new DiscreteUniformDistribution(seedGenerator, 31, 45).sample();
            vehicleType = VehicleType.PERSONAL;
        } else if (p < .14+.21) {
            inspectionTime = new DiscreteEmpiricalDistribution(
                    new int[]{35, 38, 41, 48},
                    new int[]{37, 40, 47, 52},
                    new double[]{.2, .35, .3, .15},
                    seedGenerator
            ).sample();
            vehicleType = VehicleType.VAN;
        } else {
            inspectionTime =  new DiscreteEmpiricalDistribution(
                    new int[]{37, 43, 46, 48 ,52, 56},
                    new int[]{42, 45, 47, 51, 55, 65},
                    new double[]{.05, .1, .15, .4, .25, .05},
                    seedGenerator
            ).sample();
            vehicleType = VehicleType.CARGO;
        }
    }

    public double getInspectionTime() {
        return  inspectionTime;
    }
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void arrivedInQueue(double startWaitingInQue, boolean isFifo) {
        this.isInFifoQueue = isFifo;
        this.startWaitingInQue = startWaitingInQue;
    }
    @Override
    public int compareTo(Vehicle o) {
        if (isInFifoQueue) return Double.compare(o.startWaitingInQue, this.startWaitingInQue);
        return Double.compare(this.startWaitingInQue, o.startWaitingInQue);
    }
}
