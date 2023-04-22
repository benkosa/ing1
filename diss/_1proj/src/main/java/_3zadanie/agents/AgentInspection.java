package _3zadanie.agents;

import OSPABA.*;
import _2zadanie.Vehicle;
import _2zadanie.Workers.WorkersGroup;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;
import shared.EventSimulation.Queue;

//meta! id="59"
public class AgentInspection extends Agent
{
	public final WorkersGroup<MyMessage> group2;
	public final Queue<Long, Vehicle> queueInStk = new Queue<>(5);
	final MySimulation stk;
	public AgentInspection(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		stk = (MySimulation) mySim;
		group2 = new WorkersGroup<>(stk.getWorkers2());
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
		new ManagerInspection(Id.managerInspection, mySim(), this);
		new ProcessInspection(Id.processInspection, mySim(), this);
		addOwnMessage(Mc.vehicleInspection);
		addOwnMessage(Mc.isQueueOpen);
	}
	//meta! tag="end"
}