package  org.main.stanokAgent.continualAssistants;

import OSPABA.*;
import  org.main.stanokAgent.simulation.*;
import  org.main.stanokAgent.agents.*;
import OSPABA.Process;

//meta! id="16"
public class PricesObsluhyZakaznika extends Process
{
	public PricesObsluhyZakaznika(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentNovinovehoStanku", id="17", type="Start"
	public void processStart(MessageForm message)
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
	public AgentNovinovehoStanku myAgent()
	{
		return (AgentNovinovehoStanku)super.myAgent();
	}

}