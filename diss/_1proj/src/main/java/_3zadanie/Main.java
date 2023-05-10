package _3zadanie;

import CerpaciaStanica.simulacia.SimulaciaCerpacejStanice;
import _2zadanie.Graph.Graph1;
import _2zadanie.Graph.GraphAgent1;
import _2zadanie.GuiZadanie2;
import _3zadanie.simulation.MySimulation;
import shared.Statistics.Statistics;

public class Main {

    private static final int RECEPTION_WAGE = 1100;
    private static final int CHEAP_MECHANIC_WAGE = 1500;
    private static final int EXPENSIVE_MECHANIC_WAGE = 2000;

    private static final int SIM_TIME = 8*60*60;

    private static String countWage(MySimulation stk) {
        return (stk.agentGroup1().group1.getNumberOfWorkers() * RECEPTION_WAGE +
                stk.agentInspection().groupExpensive.getNumberOfWorkers() * EXPENSIVE_MECHANIC_WAGE +
                stk.agentInspection().groupCheap.getNumberOfWorkers() * CHEAP_MECHANIC_WAGE) + " €";
    }

    private static String stdResultCsv(Statistics stat, double divide) {
        return String.format(
                "%.4f",
                stat.totalResult()/divide
        );
    }

    private static String printCsv(MySimulation stk) {
        String result = "";
        result += String.format("%d\t", stk.replicationCount());
        result += String.format("%d\t", stk.agentGroup1().group1.getNumberOfWorkers());
        if(!stk.isVerificationMode()) {
            result += String.format("%d\t", stk.agentInspection().groupExpensive.getNumberOfWorkers());
            result += String.format("%d\t", stk.agentInspection().groupCheap.getNumberOfWorkers());
            result += String.format("%s\t", countWage(stk));
            result += String.format("%.4f\t", stk.agentModel().testInputFlow.totalResult());
        } else {
            result += String.format("%d\t", stk.agentInspection().groupExpensive.getNumberOfWorkers());
        }
        result += String.format("%s\t",
                stdResultCsv(stk.agentModel().averageVehiclesInSTK, 1)
        );
        result += String.format(
                "%s\t",
                stdResultCsv(stk.averageVehicleTimeInSystem, 60)
        );
        result += String.format("%s\t",
                stdResultCsv(stk.agentGroup1().averageFreeWorker1, 1)
        );
        result += String.format("%s\t",
                stdResultCsv(stk.agentInspection().averageFreeWorker2, 1)
        );
        if(!stk.isVerificationMode()) {
            result += String.format("%s\t",
                    stdResultCsv(stk.agentInspection().averageFreeWorkerCheap, 1)
            );
        }
        result += String.format("%s\t",
                stdResultCsv(stk.agentGroup1().averageWaitingBeforeSTK, 60)
        );
        result += String.format("%s\t",
                stdResultCsv(stk.agentGroup1().averageQueueBeforeSTK, 1)
        );
        result += String.format(
                "%s\t",
                stdResultCsv(stk.agentModel().averageQueueInSystem ,1)
        );
        result = result.replace(".", ",");
        return result+"\n";
    }
    public static void main(String[] args) {

        new Thread(() -> new GuiZadanie3().start()).start();

//        new Thread(() -> new GraphAgent1(
//                1,
//                15,
//                1000,
//                0,
//                10,
//                5,
//                1
//        )).start();

//        int counter = 0;
//        final String[] result = {""};
//        for (int i = 4; i <= 20; i++) {
//            for (int j = 4; j <= 20; j++) {
//                for (int k = 4; k <= 20; k++) {
//                    counter++;
//                    MySimulation stk = new MySimulation(
//                            0,
//                            i,
//                            j,
//                            k,
//                            false,
//                            1
//                    );
//                    stk.onSimulationDidFinish((sim) -> {
//                        result[0] += printCsv(stk);
//                    });
//                    stk.simulate(1000, SIM_TIME);
//                    System.out.println(counter);
//                }
//            }
//        }
//
//        System.out.println(result[0]);



    }
}
