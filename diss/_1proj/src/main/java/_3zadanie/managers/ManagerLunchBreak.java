package _3zadanie.managers;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="146"
public class ManagerLunchBreak extends Manager
{
	public ManagerLunchBreak(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentStk", id="160", type="Request"
	public void processStartLunchBreak(MessageForm message)
	{
	}

	//meta! sender="ProcessLunchBreak", id="155", type="Finish"
	public void processFinishProcessLunchBreak(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="StartLunchBreak", id="165", type="Finish"
	public void processFinishStartLunchBreak(MessageForm message)
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
		case Mc.startLunchBreak:
			processStartLunchBreak(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.startLunchBreak:
				processFinishStartLunchBreak(message);
			break;

			case Id.processLunchBreak:
				processFinishProcessLunchBreak(message);
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
	public AgentLunchBreak myAgent()
	{
		return (AgentLunchBreak)super.myAgent();
	}

}