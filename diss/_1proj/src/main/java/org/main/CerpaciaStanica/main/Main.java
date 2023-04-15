package org.main.CerpaciaStanica.main;

import org.main.CerpaciaStanica.simulacia.SimulaciaCerpacejStanice;

public class Main
{
	public static void main(String [] args)
	{
		SimulaciaCerpacejStanice sim = new SimulaciaCerpacejStanice();
		
		sim.onSimulationWillStart(s ->{
			System.out.println("Simulating...");
		});

		sim.simulate(3, 90000000d);
	}
}
