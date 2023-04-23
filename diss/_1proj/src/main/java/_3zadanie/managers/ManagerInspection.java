package _3zadanie.managers;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="59"
public class ManagerInspection extends Manager
{
	public ManagerInspection(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentStk", id="74", type="Request"
	public void processVehicleInspection(MessageForm message)
	{
	}

	//meta! sender="ProcessInspection", id="89", type="Finish"
	public void processFinish(MessageForm message)
	{
	}

	//meta! sender="AgentStk", id="102", type="Request"
	public void processIsWorkerFree(MessageForm message)
	{
		MyMessage message1 = (MyMessage) message;
		message1.setInspectionWorkerFree(myAgent().group2.isWorkerFree());
		response(message);
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
			processVehicleInspection(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.isWorkerFree:
			processIsWorkerFree(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentInspection myAgent()
	{
		return (AgentInspection)super.myAgent();
	}

}