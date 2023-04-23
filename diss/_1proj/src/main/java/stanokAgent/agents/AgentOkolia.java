package  stanokAgent.agents;

import OSPABA.*;
import  stanokAgent.simulation.*;
import  stanokAgent.managers.*;
import  stanokAgent.continualAssistants.*;

//meta! id="3"
public class AgentOkolia extends Agent
{
	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerOkolia(Id.managerOkolia, mySim(), this);
		new PlanovaniePrichodovZakaznikov(Id.planovaniePrichodovZakaznikov, mySim(), this);
		addOwnMessage(Mc.odchodZakaznika);
	}
	//meta! tag="end"
}