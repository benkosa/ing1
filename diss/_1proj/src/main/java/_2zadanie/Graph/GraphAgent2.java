package _2zadanie.Graph;

import _3zadanie.simulation.MySimulation;

public class GraphAgent2 extends Graph{
    int workersExpensive;
    int workersCheap;
    int reception ;
    double inputFlow;
    int seed;
    int replications;
    @Override
    public double onePass(double i) {
        return i;
    }

    @Override
    protected Double doInBackground() throws Exception {


        for (final int[] i = {start}; i[0] < end; i[0]++) {
            double weRatio = (double)(workersExpensive + workersCheap) / (double)workersExpensive;
            double wcRatio = (double)(workersExpensive + workersCheap) / (double)workersCheap;

            double we = i[0] / weRatio;
            double wc = i[0] / wcRatio;

            int weInt = (int)Math.round(we);
            int wcInt = (int)Math.round(wc);

            if ( weInt == 0) weInt = 1;

            System.out.println(reception + " " + weInt + " " + wcInt);

            MySimulation stk = new MySimulation(seed, reception, weInt, wcInt, false, inputFlow
            );

            stk.onSimulationDidFinish((sim) -> {
                publish(onePass(stk.averageVehicleTimeInSystem.totalResult()/60.));
            });
            stk.simulate(replications, SIM_TIME);
        }
        return .0;
    }

    public GraphAgent2(int start, int end, int replications, int seed, int reception, int workersExpensive, int workersCheap, double inputFlow) {
        super(start, end, "počte pracovníkov skupiny 2", "priemerny čas stráveny zákazníkom v prevádzke", "Graph 2");
        this.replications = replications;
        this.seed = seed;
        this.reception = reception;
        this.workersExpensive = workersExpensive;
        this.workersCheap = workersCheap;
        this.inputFlow = inputFlow;
    }
}
