package org.main.CerpaciaStanica.manazeri;

import org.main.CerpaciaStanica.simulacia.Id;
import org.main.CerpaciaStanica.simulacia.Mc;
import org.main.CerpaciaStanica.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManazerOkolia extends Manager
{
	public ManazerOkolia(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}
	
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.init:
			zacniPlanovanieZakaznikov();
		break;
		
		case Mc.finish: // planovacPrichodovZakaznikov
			message.setCode(Mc.prichodZakaznika);
			message.setAddressee(mySim().findAgent(Id.agentModelu));
			
			notice(message);	
		break;

		case Mc.odchodZakaznika:
			;
		break;
		}
	}

	public void zacniPlanovanieZakaznikov()
	{		
		Sprava message = new Sprava(mySim());
		message.setAddressee(myAgent().findAssistant(Id.planovacPrichodovZakaznikov));
		startContinualAssistant(message);
	}
}
