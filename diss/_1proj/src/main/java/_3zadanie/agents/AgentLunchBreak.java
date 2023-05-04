package _3zadanie.agents;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="146"
public class AgentLunchBreak extends Agent
{
	public AgentLunchBreak(int id, Simulation mySim, Agent parent)
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
		new ManagerLunchBreak(Id.managerLunchBreak, mySim(), this);
		new ProcessLunchBreak(Id.processLunchBreak, mySim(), this);
		new StartLunchBreak(Id.startLunchBreak, mySim(), this);
		addOwnMessage(Mc.startLunchBreak);
	}
	//meta! tag="end"
}