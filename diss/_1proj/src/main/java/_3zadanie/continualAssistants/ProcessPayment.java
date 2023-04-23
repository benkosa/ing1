package _3zadanie.continualAssistants;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import OSPABA.Process;
import shared.Distribution.ContinuousUniformDistribution;
import shared.Distribution.TriangularDistribution;

//meta! id="85"
public class ProcessPayment extends Process
{
	private final MySimulation stk;
	private final ContinuousUniformDistribution paymentTime;
	public ProcessPayment(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		stk = (MySimulation) mySim;
		paymentTime = new ContinuousUniformDistribution(stk.seedGenerator, 65, 177);
		myAgent.addOwnMessage(Mc.processPaymentFinish);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentGroup1", id="86", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.processPaymentFinish);
		hold(paymentTime.sample(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.processPaymentFinish:
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
	public AgentGroup1 myAgent()
	{
		return (AgentGroup1)super.myAgent();
	}

}