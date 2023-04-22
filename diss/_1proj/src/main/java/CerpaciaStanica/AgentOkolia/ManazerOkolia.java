package CerpaciaStanica.AgentOkolia;

import CerpaciaStanica.simulacia.Id;
import CerpaciaStanica.simulacia.Mc;
import CerpaciaStanica.simulacia.Sprava;
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

		// povie modelu ze prisiel zakaznik
		case Mc.finish: // planovacPrichodovZakaznikov
			message.setCode(Mc.prichodZakaznika);
			message.setAddressee(mySim().findAgent(Id.agentModelu));
			
			notice(message);
		break;

		//spracovanie odchadzjuceho zakaznika
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
