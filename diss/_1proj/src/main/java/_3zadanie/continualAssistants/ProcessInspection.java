package _3zadanie.continualAssistants;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import OSPABA.Process;

//meta! id="88"
public class ProcessInspection extends Process
{
	public ProcessInspection(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		myAgent.addOwnMessage(Mc.processInspectionFinish);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentInspection", id="89", type="Start"
	public void processStart(MessageForm message)
	{
		MyMessage myMessage = (MyMessage) message;
		message.setCode(Mc.processInspectionFinish);
		hold(myMessage.getVehicle().getInspectionTime(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.processInspectionFinish:
				assistantFinished(message);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
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