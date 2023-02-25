package org.main;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        double L = 9;
        double D = 10;
        int replications = 1000000;

        int m = 0;
        int n = replications;

        Random genA = new Random();
        Random genAlfa = new Random();
        double a, alfa, y;

        for (int i = 0; i < replications; i++) {
            a = genA.nextDouble() * D;
            alfa = genAlfa.nextDouble() * Math.PI;
            y = L * Math.sin(alfa);

            if (a+y >= D) {
                m+=1;
            }
        }

        // praca s grafmi
        System.out.println((L*n*2)/(D*m));
        System.out.println(Math.PI);
    }
}