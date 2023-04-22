package _2zadanie;

public class Vehicle implements Comparable<Vehicle>{
    private final double inspectionTime;
    protected double startWaitingInQue;

    public double getStartWaitingInQue() {
        return startWaitingInQue;
    }
    private final VehicleType vehicleType;
    public long id;
    private double arrived;

    public double getArrived() {
        return arrived;
    }

    public void setArrived(double arrived) {
        this.arrived = arrived;
    }
    public Vehicle(VehicleType type, double inspectionTime, long id) {
        this.vehicleType = type;
        this.inspectionTime = inspectionTime;
        this.id = id;
    }

    public double getInspectionTime() {
        return  inspectionTime;
    }
    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void arrivedInQueue(double startWaitingInQue) {
        this.startWaitingInQue = startWaitingInQue;
    }
    @Override
    public int compareTo(Vehicle o) {
        return Double.compare(this.startWaitingInQue, o.startWaitingInQue);
    }
}
