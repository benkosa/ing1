package _3zadanie.agents;

import OSPABA.*;
import _2zadanie.Vehicle;
import _2zadanie.Workers.WorkersGroup;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;
import shared.EventSimulation.Queue;

import javax.swing.*;

//meta! id="58"
public class AgentGroup1 extends Agent
{
	public final Queue<Integer, MyMessage> queueBeforeStk = new Queue<>();
	public final Queue<Integer, MyMessage> queueAfterStk = new Queue<>();

	public final WorkersGroup<MyMessage> group1;
	final MySimulation stk;
	public AgentGroup1(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		stk = (MySimulation) mySim;
		group1 = new WorkersGroup(stk.getWorkers1());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerGroup1(Id.managerGroup1, mySim(), this);
		new IsQueueOpen(Id.isQueueOpen, mySim(), this);
		new ProcessAcceptVehicle(Id.processAcceptVehicle, mySim(), this);
		new ProcessPayment(Id.processPayment, mySim(), this);
		addOwnMessage(Mc.vehicleArrivedStk);
		addOwnMessage(Mc.vehicleInspection);
		addOwnMessage(Mc.isQueueOpen);
	}
	//meta! tag="end"
}