package _2zadanie.Graph;

import _2zadanie.STK;
import _3zadanie.simulation.MySimulation;

public class GraphAgent1 extends Graph{
    int workersExpensive;
    int workersCheap;
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
            MySimulation stk = new MySimulation(seed, i[0], workersExpensive, workersCheap, false, inputFlow
            );

            stk.onSimulationDidFinish((sim) -> {
                publish(onePass(stk.agentGroup1().averageQueueBeforeSTK.countResult()));
            });
            stk.simulate(replications, SIM_TIME);
        }
        return .0;
    }

    public GraphAgent1(int start, int end, int replications, int seed, int workersExpensive, int workersCheap, double inputFlow) {
        super(start, end, "počte pracovníkov skupiny 1", "priemerny počet čakajúcich v rade na odovzdanie auta", "Graph 1");
        this.replications = replications;
        this.seed = seed;
        this.workersExpensive = workersExpensive;
        this.workersCheap = workersCheap;
        this.inputFlow = inputFlow;
    }
}
