package shared.Statistics;

import _2zadanie.STK;
import _2zadanie.Vehicle;

public class AverageVehicleTimeInSystem<T extends Core> extends Statistics{

    public AverageVehicleTimeInSystem(T stk) {
        this.stk = stk;
    }

    private double sumTimeInSystem = 0;
    private double countVehicles;

    public SampleStandardDeviation sampleStandardDeviation = new SampleStandardDeviation();

    final private T stk;
    @Override
    protected double replicationResult() {
        double result = sumTimeInSystem/countVehicles;
        sampleStandardDeviation.countReplication(result/60.0);
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
