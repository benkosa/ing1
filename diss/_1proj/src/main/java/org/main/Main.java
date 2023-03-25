package org.main;

import org.main.shared.MonteCarlo;
import org.main.stanok.Shop;

public class Main {
    public static void main(String[] args) {
//        new GuiZadanie1().start();

//        new Thread(Pi::new).start();

//        Random genSeed = new Random(0);
//        ExponentialDistribution customerArrived = new ExponentialDistribution(genSeed, 5);
//        ExponentialDistribution customerServing = new ExponentialDistribution(genSeed, 4);
//
//        for (int i = 0; i < 12; i++) {
//            System.out.println("customerArrived "+customerArrived.sample());
//            System.out.println("customerServing "+customerServing.sample());
//        }
//

        MonteCarlo test = new Shop(1, 1000000000, 25);
        test.simulationStart();

//        final PriorityQueue<Customer> shopQueue = new PriorityQueue<>();
//
//        shopQueue.add(new Customer(20));
//        shopQueue.add(new Customer(50));
//        shopQueue.add(new Customer(5));
//
//        Customer test = shopQueue.poll();
//        Customer test2 = shopQueue.poll();
//        Customer test3 = shopQueue.poll();
//
//        System.out.println(test);


    }
}