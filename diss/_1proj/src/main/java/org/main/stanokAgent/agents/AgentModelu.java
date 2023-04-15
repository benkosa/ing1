package org.main.stanokAgent.agents;

import OSPABA.*;
import org.main.stanokAgent.simulation.*;
import org.main.stanokAgent.managers.*;
import org.main.stanokAgent.continualAssistants.*;
import org.main.stanokAgent.instantAssistants.*;

//meta! id="2"
public class AgentModelu extends Agent
{
	public AgentModelu(int id, Simulation mySim, Agent parent)
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
		new managers.ManagerModelu(Id.managerModelu, mySim(), this);
		addOwnMessage(Mc.obsluhaZakaznikaHotova);
		addOwnMessage(Mc.prichodZakaznika);
	}
	//meta! tag="end"
}
