package _3zadanie.managers;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="4"
public class ManagerSurrounding extends Manager
{
	private MySimulation stk;
	public ManagerSurrounding(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		stk = (MySimulation) mySim;
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

	//meta! sender="AgentModel", id="77", type="Notice"
	public void processInit(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="20", type="Notice"
	public void processVehicleLeft(MessageForm message)
	{
	}

	//meta! sender="CustomerArrived", id="80", type="Finish"
	public void processFinish(MessageForm message)
	{
		message.setCode(Mc.vehicleArrivedStk);
		message.setAddressee(mySim().findAgent(Id.agentModel));
		final MyMessage myMessage = (MyMessage)message;
		myMessage.setVehicle(stk.vehicleGenerator.getVehicle());
		request(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Removed from model"
	public void processVehicleArrived(MessageForm message)
	{
	}

	//meta! userInfo="Removed from model"
	public void processTest(MessageForm message)
	{
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
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.vehicleLeft:
			processVehicleLeft(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentSurrounding myAgent()
	{
		return (AgentSurrounding)super.myAgent();
	}

}