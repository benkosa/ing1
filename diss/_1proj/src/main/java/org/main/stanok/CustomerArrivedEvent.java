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

        shop.countAverageQueueSize();
        shop.shopQueue.add(customer);


        if (!shop.isServing) {
            shop.addEvent(new CustomerStartEvent(0, shop, null));
        }

    }


}
