package _2zadanie;

import shared.EventSimulation.EventSimulationCore;

public class VehicleReceivedEvent extends VehicleEvent{
    @Override
    public void execute() {
        stk.scheduleReceiveVehicleEnd(this.vehicle);
    }

    public VehicleReceivedEvent(double eventTime, EventSimulationCore myCore, Vehicle vehicle) {
        super(eventTime, myCore, vehicle);
    }
}
