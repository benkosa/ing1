package stanok;

import shared.EventSimulation.EventSimulationCore;

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
        //vytiahol z fronty
        if (waitingCustomer != null) {
            shop.addEvent(new CustomerEndEvent(shop.customerServing.sample(), shop, waitingCustomer));
            shop.countAverageQueueSize();
            shop.averageWaitingTimeInQueue.countAverageTimeInQueue(waitingCustomer.startWaitingInQue);
        //nevytiahol z fronty
        } else {
            shop.isServing = false;
        }

    }
}

