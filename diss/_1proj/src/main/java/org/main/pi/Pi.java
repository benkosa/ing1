package org.main.pi;

import org.main.SwingWorkerRealTime;

import java.util.Random;

public class Pi {

    public Pi() {}

    public double execute(final int L, final int D, final int replications) {

        double[] array = new double[replications];

        int m = 0;
        int n = 0;

        Random genA = new Random();
        Random genAlfa = new Random();
        double a, alfa, y;

        for (int i = 0; i < replications; i++) {
            n+=1;
            a = genA.nextDouble() * D;
            alfa = genAlfa.nextDouble() * Math.PI;
            y = L * Math.sin(alfa);

            if (a+y >= D) {
                m+=1;
            }
            array[i] = (double)(L*n*2)/(D*m);
        }

        return (double)(L*n*2)/(D*m);
    }
}
