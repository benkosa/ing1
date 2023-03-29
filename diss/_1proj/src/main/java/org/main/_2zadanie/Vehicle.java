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
    public long id;

    public Vehicle(SeedGenerator seedGenerator, double type, long id) {
        this.seedGenerator = seedGenerator;
        choseType(type);
        this.id = id;

    }

    private void choseType(double p) {
        if (p < .14) {
            inspectionTime =  new DiscreteUniformDistribution(seedGenerator, 31*60, 45*60).sample();
            vehicleType = VehicleType.CARGO;
        } else if (p < .14+.21) {
            inspectionTime = new DiscreteEmpiricalDistribution(
                    new int[]{35*60, 38*60, 41*60, 48*60},
                    new int[]{37*60, 40*60, 47*60, 52*60},
                    new double[]{.2, .35, .3, .15},
                    seedGenerator
            ).sample();
            vehicleType = VehicleType.VAN;
        } else {
            inspectionTime =  new DiscreteEmpiricalDistribution(
                    new int[]{37*60, 43*60, 46*60, 48 ,52*60, 56*60},
                    new int[]{42*60, 45*60, 47*60, 51*60, 55*60, 65*60},
                    new double[]{.05, .1, .15, .4, .25, .05},
                    seedGenerator
            ).sample();
            vehicleType = VehicleType.PERSONAL;
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
