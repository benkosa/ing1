package org.main.stanokAgent.agents;

import OSPABA.*;
import  org.main.stanokAgent.simulation.*;
import  org.main.stanokAgent.managers.*;
import  org.main.stanokAgent.continualAssistants.*;
import  org.main.stanokAgent.instantAssistants.*;

//meta! id="1"
public class AgentNovinovehoStanku extends Agent
{
	public AgentNovinovehoStanku(int id, Simulation mySim, Agent parent)
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
		new ManagerNovinovehoStanku(Id.managerNovinovehoStanku, mySim(), this);
		new PricesObsluhyZakaznika(Id.pricesObsluhyZakaznika, mySim(), this);
		addOwnMessage(Mc.obsluhaZakaznika);
	}
	//meta! tag="end"
}