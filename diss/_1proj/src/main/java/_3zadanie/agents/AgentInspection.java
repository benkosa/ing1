package _3zadanie.agents;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import shared.Statistics.AverageQueueLength;
import shared.Workers.WorkersGroup1;

//meta! id="59"
public class AgentInspection extends Agent
{
	public final AverageQueueLength averageFreeWorker2;
	public final WorkersGroup1<MyMessage> groupCheap;
	public final WorkersGroup1<MyMessage> groupExpensive;
	final MySimulation stk;
	public AgentInspection(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		stk = (MySimulation) mySim;
		groupExpensive = new WorkersGroup1<>(stk.getWorkersExpensive());
		groupCheap = new WorkersGroup1<>(stk.getWorkersCheap());

		averageFreeWorker2 = new AverageQueueLength(stk, groupExpensive.getWorkers());
		groupExpensive.assignStatistics(averageFreeWorker2);

	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		groupExpensive.clear();

		averageFreeWorker2.initialize();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerInspection(Id.managerInspection, mySim(), this);
		new ProcessInspection(Id.processInspection, mySim(), this);
		addOwnMessage(Mc.lunchBreakStarted);
		addOwnMessage(Mc.startLunchBreak);
		addOwnMessage(Mc.vehicleInspection);
		addOwnMessage(Mc.hireWorker);
		addOwnMessage(Mc.isWorkerFree);
	}
	//meta! tag="end"
}