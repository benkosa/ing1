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

import javax.swing.*;

//meta! id="58"
public class AgentGroup1 extends Agent
{
	public final AverageQueueLength averageFreeWorker1;
	public final Queue<Integer, MyMessage> queueBeforeStk = new Queue<>();
	public final Queue<Long, MyMessage> queueInStk = new Queue<>(5);
	public final Queue<Integer, MyMessage> queueAfterStk = new Queue<>();

	public final WorkersGroup<MyMessage> group1;
	final MySimulation stk;
	public AgentGroup1(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		stk = (MySimulation) mySim;
		group1 = new WorkersGroup<>(stk.getWorkers1());
		averageFreeWorker1 = new AverageQueueLength(stk, group1.getWorkers());
		group1.assignStatistics(averageFreeWorker1);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();

		queueInStk.clear();
		queueAfterStk.clear();
		queueBeforeStk.clear();

		group1.clear();

		averageFreeWorker1.initialize();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerGroup1(Id.managerGroup1, mySim(), this);
		new ProcessAcceptVehicle(Id.processAcceptVehicle, mySim(), this);
		new ProcessLunchBreakG1(Id.processLunchBreakG1, mySim(), this);
		new ProcessPayment(Id.processPayment, mySim(), this);
		addOwnMessage(Mc.vehicleArrivedStk);
		addOwnMessage(Mc.isWorkerFree);
		addOwnMessage(Mc.vehicleInspection);
		addOwnMessage(Mc.finishedLunchBreak);
	}
	//meta! tag="end"
}