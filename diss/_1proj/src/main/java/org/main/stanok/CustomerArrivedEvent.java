package org.main.stanok;

import org.main.shared.EventSimulation.EventSimulationCore;

public class CustomerArrivedEvent extends CustomerEvent{
    public CustomerArrivedEvent(double eventTime, EventSimulationCore myCore, Customer customer) {
        super(eventTime, myCore, customer);
    }
    @Override
    public void execute() {
        shop.scheduleNewArrival();
        //obsluhuje
        if (shop.isServing) {
            customer.startWaitingInQue = shop.getCurrentTime();
            shop.shopQueue.add(customer);
            shop.countAverageQueueSize(shop.shopQueue.size());
            //neobsluhuje - zaciatok obsluhy - planovanie konca obsluhy
        } else {
            shop.isServing = true;
            shop.addEvent(new CustomerEndEvent(shop.customerServing.sample(), shop, customer));
        }
    }


}
