package  org.main.stanokAgent.simulation;

import OSPABA.*;
import  org.main.stanokAgent.agents.*;

public class MySimulation extends Simulation
{
	public MySimulation()
	{
		init();
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
	}

	@Override
	public void simulationFinished()
	{
		// Dysplay simulation results
		super.simulationFinished();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
		setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu()));
		setAgentNovinovehoStanku(new AgentNovinovehoStanku(Id.agentNovinovehoStanku, this, agentModelu()));
	}

	private AgentModelu _agentModelu;

public AgentModelu agentModelu()
	{ return _agentModelu; }

	public void setAgentModelu(AgentModelu agentModelu)
	{_agentModelu = agentModelu; }

	private AgentOkolia _agentOkolia;

public AgentOkolia agentOkolia()
	{ return _agentOkolia; }

	public void setAgentOkolia(AgentOkolia agentOkolia)
	{_agentOkolia = agentOkolia; }

	private AgentNovinovehoStanku _agentNovinovehoStanku;

public AgentNovinovehoStanku agentNovinovehoStanku()
	{ return _agentNovinovehoStanku; }

	public void setAgentNovinovehoStanku(AgentNovinovehoStanku agentNovinovehoStanku)
	{_agentNovinovehoStanku = agentNovinovehoStanku; }
	//meta! tag="end"
}