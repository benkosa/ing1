package _3zadanie.continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import shared.Distribution.ExponentialDistribution;

//meta! id="79"
public class CustomerArrived extends Scheduler
{
	private final MySimulation mySimulation;
	public CustomerArrived(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		mySimulation = (MySimulation)mySim;
		_exp = new ExponentialDistribution(mySimulation.seedGenerator, (60.0/(23.0*mySimulation.getInputFlow()))*60.);
	}

	private static ExponentialDistribution _exp;

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentSurrounding", id="80", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.vehicleArrived);
		hold(0, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.vehicleArrived:
				double time = _exp.sample();
				if ((mySimulation.currentTime() + time) < 24300) {
					MessageForm copy = message.createCopy();
					hold(time, copy);
					assistantFinished(message);
				}
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
	public AgentSurrounding myAgent()
	{
		return (AgentSurrounding)super.myAgent();
	}

}