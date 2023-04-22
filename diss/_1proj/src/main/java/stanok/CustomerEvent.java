package stanok;

import shared.EventSimulation.EventSimulation;
import shared.EventSimulation.EventSimulationCore;

public abstract class CustomerEvent extends EventSimulation {
    Customer customer;
    public final Shop shop = (Shop)this.myCore;
    public CustomerEvent(double eventTime, EventSimulationCore myCore, Customer customer) {
        super(eventTime, myCore);
        this.customer = customer;
    }
}
