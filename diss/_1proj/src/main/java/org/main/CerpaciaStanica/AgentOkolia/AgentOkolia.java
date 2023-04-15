package org.main.CerpaciaStanica.AgentOkolia;

import org.main.CerpaciaStanica.simulacia.Id;
import org.main.CerpaciaStanica.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;

public class AgentOkolia extends Agent
{
	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);

		new ManazerOkolia(Id.manazerOkolia, mySim, this);
		
		new PlanovacPrichodovZakaznikov(Id.planovacPrichodovZakaznikov, mySim, this);

		addOwnMessage(Mc.init);
		addOwnMessage(Mc.novyZakaznik);
		addOwnMessage(Mc.odchodZakaznika);
	}
}
