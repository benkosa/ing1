package shared.Statistics;

import _2zadanie.STK;
import _2zadanie.Vehicle;

public class AverageVehicleTimeInSystem<T extends Core> extends Statistics{

    public AverageVehicleTimeInSystem(T stk) {
        this.stk = stk;
    }

    private double sumTimeInSystem = 0;
    private double countVehicles;

    final private T stk;
    @Override
    protected double replicationResult() {
        return sumTimeInSystem/countVehicles;
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
