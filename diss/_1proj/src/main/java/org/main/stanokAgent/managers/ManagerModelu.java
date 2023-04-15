package  org.main.stanokAgent.managers;

import OSPABA.*;
import  org.main.stanokAgent.simulation.*;
import  org.main.stanokAgent.agents.*;

//meta! id="2"
public class ManagerModelu extends Manager
{
	public ManagerModelu(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentNovinovehoStanku", id="24", type="Notice"
	public void processObsluhaZakaznikaHotova(MessageForm message)
	{
	}

	//meta! sender="AgentOkolia", id="33", type="Notice"
	public void processPrichodZakaznika(MessageForm message)
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
		case Mc.prichodZakaznika:
			processPrichodZakaznika(message);
		break;

		case Mc.obsluhaZakaznikaHotova:
			processObsluhaZakaznikaHotova(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentModelu myAgent()
	{
		return (AgentModelu)super.myAgent();
	}

}