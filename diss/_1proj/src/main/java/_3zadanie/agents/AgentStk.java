package _3zadanie.agents;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="17"
public class AgentStk extends Agent
{
	public AgentStk(int id, Simulation mySim, Agent parent)
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
		new ManagerStk(Id.managerStk, mySim(), this);
		addOwnMessage(Mc.vehicleArrivedStk);
		addOwnMessage(Mc.lunchBreakStarted);
		addOwnMessage(Mc.startLunchBreak);
		addOwnMessage(Mc.isWorkerFree);
		addOwnMessage(Mc.hireWorker);
		addOwnMessage(Mc.vehicleInspection);
		addOwnMessage(Mc.finishedLunchBreak);
	}
	//meta! tag="end"
}