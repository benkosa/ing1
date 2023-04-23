package _3zadanie.managers;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="5"
public class ManagerModel extends Manager
{
	public ManagerModel(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentStk", id="21", type="Response"
	public void processVehicleArrivedStk(MessageForm message)
	{

		message.setCode(Mc.vehicleLeft);
		message.setAddressee(Id.agentSurrounding);
		notice(message);
	}

	//meta! sender="AgentSurrounding", id="19", type="Notice"
	public void processVehicleArrived(MessageForm message)
	{
		message.setCode(Mc.vehicleArrivedStk);
		message.setAddressee(mySim().findAgent(Id.agentStk));

		request(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.vehicleArrived:
			processVehicleArrived(message);
		break;

		case Mc.vehicleArrivedStk:
			processVehicleArrivedStk(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentModel myAgent()
	{
		return (AgentModel)super.myAgent();
	}

}