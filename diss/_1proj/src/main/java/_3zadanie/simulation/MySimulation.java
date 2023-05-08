package _3zadanie.simulation;

import OSPABA.*;
import _3zadanie.agents.*;
import _3zadanie.support.VehicleGenerator;
import shared.Distribution.SeedGenerator;
import shared.Statistics.AverageVehicleTimeInSystem;
import shared.Statistics.Core;

public class MySimulation extends Simulation implements Core
{
	private final int workers1;

	@Override
	public double getCurrentTime() {
		return currentTime();
	}

	private final int workersExpensive;
	private final int workersCheap;

	private double inputFlow = 1;

	public double getInputFlow() {
		return inputFlow;
	}

	public boolean isVerificationMode() {
		return verificationMode;
	}

	private final boolean verificationMode;

	public int getWorkersCheap() {
		return workersCheap;
	}

	public final SeedGenerator seedGenerator;
	public final VehicleGenerator vehicleGenerator;

	public final AverageVehicleTimeInSystem <MySimulation> averageVehicleTimeInSystem;

	public MySimulation(int seed, int workers1, int workersExpensive, int workersCheap, boolean verificationMode, double inputFlow)
	{
		seedGenerator = new SeedGenerator(seed);
		vehicleGenerator = new VehicleGenerator(seedGenerator);
		this.workers1 = workers1;
		this.workersExpensive = workersExpensive;
		this.workersCheap = workersCheap;
		this.verificationMode = verificationMode;
		averageVehicleTimeInSystem = new AverageVehicleTimeInSystem<>(this);
		this.inputFlow = inputFlow;
		init();
	}

	public int getWorkers1() {
		return workers1;
	}

	public int getWorkersExpensive() {
		return workersExpensive;
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
		averageVehicleTimeInSystem.initialize();
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
		averageVehicleTimeInSystem.countResult();
		_agentModel.averageVehiclesInSTK.countResult();
		_agentGroup1.averageFreeWorker1.countResult();
		_agentInspection.averageFreeWorker2.countResult();
		_agentInspection.averageFreeWorkerCheap.countResult();
		_agentGroup1.averageWaitingBeforeSTK.countResult();
		_agentGroup1.averageQueueBeforeSTK.countResult();
		_agentModel.averageQueueInSystem.countResult();
		_agentModel.testInputFlow.countResult();
	}

	@Override
	public void simulationFinished()
	{
		// Dysplay simulation results
		super.simulationFinished();
		System.out.println(averageVehicleTimeInSystem.totalResult()/60.0);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModel(new AgentModel(Id.agentModel, this, null));
		setAgentStk(new AgentStk(Id.agentStk, this, agentModel()));
		setAgentLunchBreak(new AgentLunchBreak(Id.agentLunchBreak, this, agentStk()));
		setAgentGroup1(new AgentGroup1(Id.agentGroup1, this, agentStk()));
		setAgentInspection(new AgentInspection(Id.agentInspection, this, agentStk()));
		setAgentSurrounding(new AgentSurrounding(Id.agentSurrounding, this, agentModel()));
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

	private AgentLunchBreak _agentLunchBreak;

public AgentLunchBreak agentLunchBreak()
	{ return _agentLunchBreak; }

	public void setAgentLunchBreak(AgentLunchBreak agentLunchBreak)
	{_agentLunchBreak = agentLunchBreak; }

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

	private AgentSurrounding _agentSurrounding;

public AgentSurrounding agentSurrounding()
	{ return _agentSurrounding; }

	public void setAgentSurrounding(AgentSurrounding agentSurrounding)
	{_agentSurrounding = agentSurrounding; }
	//meta! tag="end"
}