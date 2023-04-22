package _3zadanie.instantAssistants;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;

//meta! id="95"
public class IsQueueOpen extends Query
{
	public IsQueueOpen(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
	}

	@Override
	public AgentGroup1 myAgent()
	{
		return (AgentGroup1)super.myAgent();
	}

}
