package  org.main.stanokAgent.managers;

import OSPABA.*;
import  org.main.stanokAgent.simulation.*;
import  org.main.stanokAgent.agents.*;

//meta! id="1"
public class ManagerNovinovehoStanku extends Manager
{
	public ManagerNovinovehoStanku(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentModelu", id="28", type="Notice"
	public void processObsluhaZakaznika(MessageForm message)
	{
	}

	//meta! sender="PricesObsluhyZakaznika", id="17", type="Finish"
	public void processFinish(MessageForm message)
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
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.obsluhaZakaznika:
			processObsluhaZakaznika(message);
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