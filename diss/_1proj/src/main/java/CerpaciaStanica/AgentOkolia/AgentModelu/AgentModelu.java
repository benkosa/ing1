package CerpaciaStanica.AgentOkolia.AgentModelu;

import CerpaciaStanica.simulacia.Id;
import CerpaciaStanica.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.Simulation;
import CerpaciaStanica.simulacia.Sprava;

public class AgentModelu extends Agent
{
	public AgentModelu(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		
		new ManazerModelu(Id.manazerModelu, mySim, this);
		
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.prichodZakaznika);
		addOwnMessage(Mc.obsluhaZakaznikaHotova);
	}
	
	public void spustiSimulaciu()
	{
		Sprava message = new Sprava(mySim());
		message.setCode(Mc.init);
		message.setAddressee(this);
		manager().notice(message);
	}
}
