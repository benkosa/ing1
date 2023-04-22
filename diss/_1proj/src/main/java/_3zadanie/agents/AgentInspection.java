package _3zadanie.agents;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="59"
public class AgentInspection extends Agent
{
	public AgentInspection(int id, Simulation mySim, Agent parent)
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
		new ManagerInspection(Id.managerInspection, mySim(), this);
		new ProcessInspection(Id.processInspection, mySim(), this);
		addOwnMessage(Mc.vehicleInspection);
		addOwnMessage(Mc.isQueueOpen);
	}
	//meta! tag="end"
}