package _3zadanie.continualAssistants;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import OSPABA.Process;
import shared.Workers.Worker;

//meta! id="124"
public class ProcessLunchBreakG1 extends Process
{
	final int lunchBreakStart = 60;
	final int lunchBreakDuration = 30*60;
	public ProcessLunchBreakG1(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		myAgent.addOwnMessage(Mc.lunchBreakStartedG1);
		myAgent.addOwnMessage(Mc.processLunchBreakG1Finish);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		MyMessage message =  new MyMessage(mySim());
		message.setCode(Mc.lunchBreakStartedG1);
		hold(lunchBreakStart, message);
	}

	//meta! sender="AgentGroup1", id="125", type="Start"
	public void processStart(MessageForm message)
	{
	}

	private void startLunchBreak(MessageForm message) {
		message.setCode(Mc.processLunchBreakG1Finish);
		hold(lunchBreakDuration, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.lunchBreakStartedG1: {
				System.out.println("lunchBreakStarted " + mySim().currentTime());

				myAgent().group1.getWorkers().forEach(worker -> {
					MyMessage myMessage = (MyMessage)message.createCopy();
					myMessage.setWorker(worker);
					startLunchBreak(myMessage);
				});

				myAgent().group1.startLunchBreak();
				break;
			}
			case Mc.processLunchBreakG1Finish: {
				System.out.println("lunch break FINISHED ");
				MyMessage myMessage = (MyMessage)message;
				Worker worker = myMessage.getWorker();
				myAgent().group1.endLunchBreakWorker(worker);
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
	public AgentGroup1 myAgent()
	{
		return (AgentGroup1)super.myAgent();
	}

}
