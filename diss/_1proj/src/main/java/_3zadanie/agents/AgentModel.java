package _3zadanie.agents;

import CerpaciaStanica.simulacia.Sprava;
import OSPABA.*;
import _2zadanie.Vehicle;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;
import shared.EventSimulation.Queue;

import java.util.LinkedList;

//meta! id="5"
public class AgentModel extends Agent
{
	public LinkedList<MyMessage> arrivedVehicles = new LinkedList<>();
	public LinkedList<MyMessage> leftVehicles = new LinkedList<>();
	public AgentModel(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
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
		new ManagerModel(Id.managerModel, mySim(), this);
		addOwnMessage(Mc.vehicleArrivedStk);
		addOwnMessage(Mc.vehicleArrived);
	}
	//meta! tag="end"
}