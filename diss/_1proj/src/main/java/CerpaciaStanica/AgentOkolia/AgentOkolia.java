package CerpaciaStanica.AgentOkolia;

import CerpaciaStanica.simulacia.Id;
import CerpaciaStanica.simulacia.Mc;
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
