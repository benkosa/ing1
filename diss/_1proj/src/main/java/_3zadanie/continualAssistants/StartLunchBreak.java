package _3zadanie.continualAssistants;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;

//meta! id="164"
public class StartLunchBreak extends Scheduler
{
	final static int LUNCH_BREAK_START = 60;
	public StartLunchBreak(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		myAgent().addOwnMessage(Mc.lunchBreakStartedFinnish);

		MyMessage myMessage = new MyMessage(mySim());
		myMessage.setAddressee(myAgent().findAssistant(Id.startLunchBreak));
		myAgent().manager().startContinualAssistant(myMessage);
	}

	//meta! sender="AgentLunchBreak", id="165", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.lunchBreakStartedFinnish);
		hold(LUNCH_BREAK_START, message);

	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.lunchBreakStartedFinnish: {
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
