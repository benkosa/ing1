package org.main.shared.Statistics;

import org.main._2zadanie.STK;
import org.main._2zadanie.Vehicle;

public class AverageVehicleTimeInSystem extends Statistics{

    public AverageVehicleTimeInSystem(STK stk) {
        this.stk = stk;
    }

    private double sumTimeInSystem = 0;
    private long countVehicles;

    public SampleStandardDeviation sampleStandardDeviation = new SampleStandardDeviation();

    final private STK stk;
    @Override
    protected double replicationResult() {
        double result = (sumTimeInSystem/countVehicles);
        sampleStandardDeviation.countReplication(result/60);
        return result;
    }

    @Override
    public void initialize() {
        sumTimeInSystem = 0;
        countVehicles = 0;
    }

    public void vehicleLeft(Vehicle vehicle) {
        sumTimeInSystem += stk.getCurrentTime() - vehicle.getArrived();
        countVehicles +=1;
    }

}
