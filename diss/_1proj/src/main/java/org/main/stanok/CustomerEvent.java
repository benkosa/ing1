package org.main.stanok;

import org.main.shared.EventSimulation.EventSimulation;
import org.main.shared.EventSimulation.EventSimulationCore;

public abstract class CustomerEvent extends EventSimulation {
    Customer customer;
    public final Shop shop = (Shop)this.myCore;
    public CustomerEvent(double eventTime, EventSimulationCore myCore, Customer customer) {
        super(eventTime, myCore);
        this.customer = customer;
    }
}
