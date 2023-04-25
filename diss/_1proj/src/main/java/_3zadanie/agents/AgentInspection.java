package _3zadanie.agents;

import OSPABA.*;
import _2zadanie.Vehicle;
import _2zadanie.Workers.WorkersGroup;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;
import shared.EventSimulation.Queue;
import shared.Statistics.AverageQueueLength;

//meta! id="59"
public class AgentInspection extends Agent
{
	public final AverageQueueLength averageFreeWorker2;
	public final WorkersGroup<MyMessage> group2;
	final MySimulation stk;
	public AgentInspection(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		stk = (MySimulation) mySim;
		group2 = new WorkersGroup<>(stk.getWorkers2());

		averageFreeWorker2 = new AverageQueueLength(stk, group2.getWorkers());
		group2.assignStatistics(averageFreeWorker2);

	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		group2.clear();

		averageFreeWorker2.initialize();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerInspection(Id.managerInspection, mySim(), this);
		new ProcessLunchBreakG2(Id.processLunchBreakG2, mySim(), this);
		new ProcessInspection(Id.processInspection, mySim(), this);
		addOwnMessage(Mc.isWorkerFree);
		addOwnMessage(Mc.hireWorker);
		addOwnMessage(Mc.vehicleInspection);
	}
	//meta! tag="end"
}