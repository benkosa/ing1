package org.main.shared.Statistics;
public class AverageVehiclesInSTK extends Statistics {
    private long vehicleArrived;
    private long vehicleLeft;

    @Override
    protected double replicationResult() {
        if (vehicleLeft > vehicleArrived) {
            System.out.println("warning: more vehicles left than arrived");
        }
        return vehicleArrived - vehicleLeft;
    }

    @Override
    public void initialize() {
        vehicleLeft = 0;
        vehicleArrived = 0;
    }

    public void vehicleArrived() {
        vehicleArrived+=1;
    }

    public void vehicleLeft() {
        vehicleLeft+=1;
    }
}
