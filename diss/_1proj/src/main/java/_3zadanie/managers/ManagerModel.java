package _3zadanie.managers;

import OSPABA.*;
import _2zadanie.STK;
import _2zadanie.Vehicle;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;
import shared.Statistics.AverageVehicleTimeInSystem;

//meta! id="5"
public class ManagerModel extends Manager
{
	private MySimulation stk;
	public ManagerModel(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
		stk = (MySimulation) mySim;
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
		final MyMessage myMessage = (MyMessage)message;
		stk.averageVehicleTimeInSystem.vehicleLeft(myMessage.getVehicle());
		myAgent().leftVehicles.add(myMessage);

		myAgent().averageVehiclesInSTK.vehicleLeft();
		myAgent().queueInSystem.move(myMessage.getId());
		myAgent().averageQueueInSystem.countAverageQueueLength();

		message.setCode(Mc.vehicleLeft);
		message.setAddressee(Id.agentSurrounding);
		notice(message);
	}

	//meta! sender="AgentSurrounding", id="19", type="Notice"
	public void processVehicleArrived(MessageForm message)
	{
		final MyMessage myMessage = (MyMessage)message;
		myMessage.getVehicle().setArrived(mySim().currentTime());

		message.setCode(Mc.vehicleArrivedStk);
		message.setAddressee(mySim().findAgent(Id.agentStk));

		myAgent().averageVehiclesInSTK.vehicleArrived();
		myAgent().queueInSystem.addQueueLocked(myMessage.getId(), myMessage);
		myAgent().averageQueueInSystem.countAverageQueueLength();

		myAgent().arrivedVehicles.add(myMessage);

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
		case Mc.vehicleArrivedStk:
			processVehicleArrivedStk(message);
		break;

		case Mc.vehicleArrived:
			processVehicleArrived(message);
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