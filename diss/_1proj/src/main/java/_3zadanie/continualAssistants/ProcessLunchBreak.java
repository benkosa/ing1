package _3zadanie.continualAssistants;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import OSPABA.Process;

//meta! id="154"
public class ProcessLunchBreak extends Process
{
	final static int LUNCH_BREAK_DURATION = 30*60;
	public ProcessLunchBreak(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		myAgent().addOwnMessage(Mc.processLunchBreakFinnish);
	}

	//meta! sender="AgentLunchBreak", id="155", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.processLunchBreakFinnish);
		hold(LUNCH_BREAK_DURATION, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.processLunchBreakFinnish: {
				assistantFinished(message);
				break;
			}
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
	public AgentLunchBreak myAgent()
	{
		return (AgentLunchBreak)super.myAgent();
	}

}