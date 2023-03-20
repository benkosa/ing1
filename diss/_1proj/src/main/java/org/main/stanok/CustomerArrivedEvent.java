package org.main.stanok;

import org.main.shared.EventSimulation.EventSimulationCore;

public class CustomerArrivedEvent extends CustomerEvent{
    public CustomerArrivedEvent(double eventTime, EventSimulationCore myCore, Customer customer) {
        super(eventTime, myCore, customer);
    }
    @Override
    public void execute() {
        shop.scheduleNewArrival();
        customer.startWaitingInQue = shop.getCurrentTime();
        //obsluhuje
        if (shop.isServing) {
            shop.shopQueue.add(customer);
            //neobsluhuje - zaciatok obsluhy - planovanie konca obsluhy
        } else {
            shop.isServing = true;
            shop.averageWaitingTimeInQueue.countAverageTimeInQueue(customer.startWaitingInQue);
            shop.addEvent(new CustomerEndEvent(shop.customerServing.sample(), shop, customer));
        }
        shop.countAverageQueueSize();
    }


}
