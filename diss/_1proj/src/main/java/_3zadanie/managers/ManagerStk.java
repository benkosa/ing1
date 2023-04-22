package _3zadanie.managers;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="17"
public class ManagerStk extends Manager
{
	public ManagerStk(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentGroup1", id="65", type="Response"
	public void processVehicleArrivedStkAgentGroup1(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="21", type="Request"
	public void processVehicleArrivedStkAgentModel(MessageForm message)
	{
	}

	//meta! sender="AgentGroup1", id="69", type="Request"
	public void processVehicleInspectionAgentGroup1(MessageForm message)
	{
	}

	//meta! sender="AgentInspection", id="74", type="Response"
	public void processVehicleInspectionAgentInspection(MessageForm message)
	{
	}

	//meta! sender="AgentInspection", id="102", type="Response"
	public void processIsQueueOpenAgentInspection(MessageForm message)
	{
	}

	//meta! sender="AgentGroup1", id="98", type="Request"
	public void processIsQueueOpenAgentGroup1(MessageForm message)
	{
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
		case Mc.vehicleInspection:
			switch (message.sender().id())
			{
			case Id.agentGroup1:
				processVehicleInspectionAgentGroup1(message);
			break;

			case Id.agentInspection:
				processVehicleInspectionAgentInspection(message);
			break;
			}
		break;

		case Mc.isQueueOpen:
			switch (message.sender().id())
			{
			case Id.agentInspection:
				processIsQueueOpenAgentInspection(message);
			break;

			case Id.agentGroup1:
				processIsQueueOpenAgentGroup1(message);
			break;
			}
		break;

		case Mc.vehicleArrivedStk:
			switch (message.sender().id())
			{
			case Id.agentGroup1:
				processVehicleArrivedStkAgentGroup1(message);
			break;

			case Id.agentModel:
				processVehicleArrivedStkAgentModel(message);
			break;
			}
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentStk myAgent()
	{
		return (AgentStk)super.myAgent();
	}

}
