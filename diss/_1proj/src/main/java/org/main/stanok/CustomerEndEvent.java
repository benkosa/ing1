package org.main.stanok;

import org.main.shared.EventSimulation.EventSimulationCore;

/**
 * planovanie konca obsluhy zakaznika a zaciatok obsluhy dalsieho zakaznika
 */
public class CustomerEndEvent extends CustomerEvent{
    public CustomerEndEvent(double eventTime, EventSimulationCore myCore, Customer customer) {
        super(eventTime, myCore, customer);
    }
    @Override
    public void execute() {
        shop.isServing = false;
        if (shop.shopQueue.size() > 0) {
            shop.addEvent(new CustomerStartEvent(0,shop, null));
        }

    }
}

