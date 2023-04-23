package _3zadanie.agents;

import OSPABA.*;
import _3zadanie.simulation.*;
import _3zadanie.managers.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="4"
public class AgentSurrounding extends Agent
{
	public AgentSurrounding(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.vehicleArrived);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		spustiSimulaciu();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerSurrounding(Id.managerSurrounding, mySim(), this);
		new CustomerArrived(Id.customerArrived, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.vehicleLeft);
	}
	//meta! tag="end"

	public void spustiSimulaciu()
	{
		MyMessage myMessage = new MyMessage(mySim());
		myMessage.setAddressee(findAssistant(Id.customerArrived));

		System.out.println(Id.customerArrived);
		System.out.println(manager().id());

		manager().startContinualAssistant(myMessage);
	}
}