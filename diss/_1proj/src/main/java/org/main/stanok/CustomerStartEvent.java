package org.main.stanok;

import org.main.shared.EventSimulation.EventSimulationCore;

public class CustomerStartEvent extends CustomerEvent{
    public CustomerStartEvent(double eventTime, EventSimulationCore myCore, Customer customer) {
        super(eventTime, myCore, customer);
    }
    @Override
    public void execute() {

    }
}
