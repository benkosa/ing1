package _3zadanie;

import CerpaciaStanica.simulacia.SimulaciaCerpacejStanice;
import _2zadanie.GuiZadanie2;
import _3zadanie.simulation.MySimulation;

public class Main {
    public static void main(String[] args) {
        MySimulation sim = new MySimulation(10);

        sim.onSimulationWillStart(s ->{
            System.out.println("Simulating...");
        });

        sim.simulate(1, 24300);
    }
}
