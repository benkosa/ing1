package _3zadanie.agents;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import shared.Statistics.AverageQueueLength;
import shared.Workers.WorkersGroupLunchBreak;

//meta! id="59"
public class AgentInspection extends Agent
{
	public final AverageQueueLength averageFreeWorker2;
	public final AverageQueueLength averageFreeWorkerCheap;
	public final WorkersGroupLunchBreak<MyMessage> groupCheap;
	public final WorkersGroupLunchBreak<MyMessage> groupExpensive;
	final MySimulation stk;
	public AgentInspection(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		stk = (MySimulation) mySim;
		groupExpensive = new WorkersGroupLunchBreak<>(stk.getWorkersExpensive(), stk);
		groupCheap = new WorkersGroupLunchBreak<>(stk.getWorkersCheap(), stk);

		averageFreeWorker2 = new AverageQueueLength(stk, groupExpensive.getWorkers());
		averageFreeWorkerCheap = new AverageQueueLength(stk, groupCheap.getWorkers());
		groupExpensive.assignStatistics(averageFreeWorker2);
		groupCheap.assignStatistics(averageFreeWorkerCheap);
		groupExpensive.setExpensive(true);

	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		groupExpensive.clear();
		groupCheap.clear();

		averageFreeWorker2.initialize();
		averageFreeWorkerCheap.initialize();
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