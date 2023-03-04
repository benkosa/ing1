package org.main;

import org.main._1zadanie.GuiZadanie1;
import org.main.shared.Distribution.DiscreteEmpiricalDistribution;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
//        GuiZadanie1 test = new GuiZadanie1();
//
//        test.start();

        DiscreteEmpiricalDistribution e_c = new DiscreteEmpiricalDistribution(
                new int[] {230, 244, 281 },
                new int[] {243, 280, 350 },
                new double[] {.3, .5, .2 },
                new Random(0)
        );

        for (int i = 0; i < 100; i++) {
            e_c.sample();
        }
    }
}