package pi;

import shared.MonteCarlo;

import java.util.Random;

public class Pi extends MonteCarlo {

    final int L = 9;
    final int D = 10;
    long m = 0;
    long n = 0;

    Random genGen = new Random();
    Random genA = new Random(genGen.nextInt());
    Random genAlfa = new Random(genGen.nextInt());
    double a, alfa, y;

    public Pi() {
        super(1000000000L);
        start();
    }

    public void start() {
        System.out.println(this.simulationStart());
    }

    @Override
    public double onePass() {
        n+=1;
        a = genA.nextDouble() * D;
        alfa = genAlfa.nextDouble() * Math.PI;
        y = L * Math.sin(alfa);

        if (a+y >= D) {
            m+=1;
        }
        if (m > 0) {
            return((double) (L * n * 2) / (D * m));
        }
        return 0;
    }

    @Override
    public void beforeSimulation() {

    }

    @Override
    public void afterSimulation() {

    }

    @Override
    protected void beforeReplication() {

    }

    @Override
    protected void afterReplication() {

    }
}
