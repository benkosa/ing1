package shared.Statistics;
public class AverageVehiclesInSTK extends Statistics {
    private long vehicleArrived;
    private long vehicleLeft;

    private double totalArrived;
    private double totalLeft;

    @Override
    protected double replicationResult() {
        if (vehicleLeft > vehicleArrived) {
            System.out.println("warning: more vehicles left than arrived");
        }
        totalArrived += vehicleArrived;
        totalLeft += vehicleLeft;
        return (double)vehicleArrived - vehicleLeft;
    }

    public void testTotalInOut() {
        System.out.println(totalArrived/getCountReplications());
        System.out.println(totalLeft/getCountReplications());
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
