package org.main.CerpaciaStanica.agenti;

import org.main.CerpaciaStanica.asistenti.PlanovacPrichodovZakaznikov;
import org.main.CerpaciaStanica.manazeri.ManazerOkolia;
import org.main.CerpaciaStanica.simulacia.Id;
import org.main.CerpaciaStanica.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.ContinualAssistant;
import OSPABA.Simulation;

public class AgentOkolia extends Agent
{
	private ContinualAssistant _planovacPrichodovZakaznikov;

	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);

		new ManazerOkolia(Id.manazerOkolia, mySim, this);
		
		_planovacPrichodovZakaznikov = new PlanovacPrichodovZakaznikov(Id.planovacPrichodovZakaznikov, mySim, this);

		addOwnMessage(Mc.init);
		addOwnMessage(Mc.novyZakaznik);
		addOwnMessage(Mc.odchodZakaznika);
	}
	
	public void zacniPlanovanieZakaznikov()
	{
		((ManazerOkolia)manager()).zacniPlanovanieZakaznikov();
	}
	
	public ContinualAssistant planovacPrichodovZakaznikov()
	{ return _planovacPrichodovZakaznikov; }
}
