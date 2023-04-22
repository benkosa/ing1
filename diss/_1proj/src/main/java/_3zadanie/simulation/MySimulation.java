package _3zadanie.simulation;

import OSPABA.*;
import _3zadanie.agents.*;

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
		setAgentModel(new AgentModel(Id.agentModel, this, null));
		setAgentStk(new AgentStk(Id.agentStk, this, agentModel()));
		setAgentSurrounding(new AgentSurrounding(Id.agentSurrounding, this, agentModel()));
		setAgentGroup1(new AgentGroup1(Id.agentGroup1, this, agentStk()));
		setAgentInspection(new AgentInspection(Id.agentInspection, this, agentStk()));
	}

	private AgentModel _agentModel;

public AgentModel agentModel()
	{ return _agentModel; }

	public void setAgentModel(AgentModel agentModel)
	{_agentModel = agentModel; }

	private AgentStk _agentStk;

public AgentStk agentStk()
	{ return _agentStk; }

	public void setAgentStk(AgentStk agentStk)
	{_agentStk = agentStk; }

	private AgentSurrounding _agentSurrounding;

public AgentSurrounding agentSurrounding()
	{ return _agentSurrounding; }

	public void setAgentSurrounding(AgentSurrounding agentSurrounding)
	{_agentSurrounding = agentSurrounding; }

	private AgentGroup1 _agentGroup1;

public AgentGroup1 agentGroup1()
	{ return _agentGroup1; }

	public void setAgentGroup1(AgentGroup1 agentGroup1)
	{_agentGroup1 = agentGroup1; }

	private AgentInspection _agentInspection;

public AgentInspection agentInspection()
	{ return _agentInspection; }

	public void setAgentInspection(AgentInspection agentInspection)
	{_agentInspection = agentInspection; }
	//meta! tag="end"
}