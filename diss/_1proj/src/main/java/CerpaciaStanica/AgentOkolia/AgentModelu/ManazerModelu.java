package CerpaciaStanica.AgentOkolia.AgentModelu;

import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import CerpaciaStanica.simulacia.Id;
import CerpaciaStanica.simulacia.Mc;

/**
 * spracovanie prichadzajucich zakaznikov odchadzajucich zakaznikov
 *
 * povie manazerovi okolia ze ma zacat planovat zakaznikov
 */
public class ManazerModelu extends Manager
{	
	public ManazerModelu(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
			// povie manazerovi okolia ze ma zacat planovat zakaznikov
		case Mc.init:
			message.setAddressee(mySim().findAgent(Id.agentOkolia));
			notice(message);
		break;

		// preda prichadazjuceho zakaznika do cepracej stanice
		case Mc.prichodZakaznika:
			message.setCode(Mc.obsluhaZakaznika);
			message.setAddressee(mySim().findAgent(Id.agentCerpacejStanice));
			
			request(message);
		break;

		// povie agentovi okolia ze zakaznik opusta stanicu
		case Mc.obsluhaZakaznikaHotova:
			message.setCode(Mc.odchodZakaznika);
			message.setAddressee(mySim().findAgent(Id.agentOkolia));
			
			notice(message);
		break;
		}
	}

}
