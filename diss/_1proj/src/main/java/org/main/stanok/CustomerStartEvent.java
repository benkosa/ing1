package org.main.stanok;

import org.main.shared.EventSimulation.EventSimulationCore;

public class CustomerStartEvent  extends CustomerEvent{
    @Override
    public void execute() {
        shop.countAverageQueueSize();
        customer = shop.shopQueue.poll();

        if (customer == null) {
            System.out.println("warning: CustomerStartEvent customer not found");
            return;
        }

        shop.averageWaitingTimeInQueue.countAverageTimeInQueue(customer.startWaitingInQue);

        if (!shop.isServing) {
            shop.isServing = true;
            shop.addEvent(new CustomerEndEvent(shop.customerServing.sample(), shop, customer));
        }
    }

    public CustomerStartEvent(double eventTime, EventSimulationCore myCore, Customer customer) {
        super(eventTime, myCore, customer);
    }
}
