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
        Customer waitingCustomer = shop.shopQueue.poll();
        shop.countAverageQueueSize(shop.shopQueue.size());
        //vytiahol z fronty
        if (waitingCustomer != null) {
            shop.addEvent(new CustomerEndEvent(shop.customerServing.sample(), shop, waitingCustomer));
            shop.countAverageTimeInQueue(waitingCustomer.startWaitingInQue);
        //nevytiahol z fronty
        } else {
            shop.isServing = false;
        }

    }
}

