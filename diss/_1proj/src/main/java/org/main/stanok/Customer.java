package org.main.stanok;

public class Customer implements Comparable<Customer>{
    protected double startWaitingInQue;

    public Customer(double startWaitingInQue) {
        this.startWaitingInQue = startWaitingInQue;
    }

    @Override
    public int compareTo(Customer customer) {
        return Double.compare(this.startWaitingInQue, customer.startWaitingInQue);
    }
}
