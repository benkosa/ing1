package _3zadanie.managers;

import OSPABA.*;
import _2zadanie.Vehicle;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;

//meta! id="58"
public class ManagerGroup1 extends Manager
{
	private final MySimulation stk;
	public ManagerGroup1(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
		stk = (MySimulation) mySim;
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentStk", id="65", type="Request"
	public void processVehicleArrivedStk(MessageForm message)
	{
		message.setCode(Mc.isQueueOpen);
		message.setAddressee(Id.agentStk);
		request(message);
	}

	//meta! sender="AgentStk", id="69", type="Response"
	public void processVehicleInspection(MessageForm message)
	{
	}

	//meta! sender="ProcessAcceptVehicle", id="84", type="Finish"
	public void processFinishProcessAcceptVehicle(MessageForm message)
	{
	}

	//meta! sender="ProcessPayment", id="86", type="Finish"
	public void processFinishProcessPayment(MessageForm message)
	{
	}

	//meta! sender="AgentStk", id="98", type="Response"
	public void processIsQueueOpen(MessageForm message)
	{
		final MyMessage myMessage = (MyMessage)message;
		final Vehicle vehicle = myMessage.getVehicle();
		//if ()
		System.out.println("vehicle arrived: " + myMessage.getVehicle().id + " : " + message.deliveryTime());
				// code from event simulation
		myMessage.getVehicle().setArrived(stk.currentTime());

		// je volny worker
		if (myAgent().group1.isWorkerFree()) {
			//niekto caka na platbu
			if (myAgent().queueAfterStk.getSize() > 0) {
				final MyMessage newVehicle = myAgent().queueAfterStk.poll();
				myAgent().group1.hireWorker(newVehicle);
				//TODO START PAYMENT
				//stk.scheduleStartPayment(newVehicle);
				// ideme priamo na receive
			} else if (stk.isSpaceInsideStk() && myAgent().queueBeforeStk.getSize() == 0){
				vehicle.startWaitingInQue = stk.getCurrentTime();
				stk.averageWaitingBeforeSTK.countAverageTimeInQueue(vehicle.startWaitingInQue);
				stk.group1.hireWorker(this.vehicle);
				stk.arrivedInStkQueue(this.vehicle);
				stk.scheduleReceiveVehicle(this.vehicle);
				stk.averageQueueBeforeSTK.countAverageQueueLength();

			}else if (stk.isSpaceInsideStk() && stk.queueBeforeStk.getSize() > 0) {
				final Vehicle newVehicle = stk.queueBeforeStk.poll();
				stk.group1.hireWorker(newVehicle);
				stk.arrivedInStkQueue(newVehicle);
				stk.scheduleReceiveVehicle(newVehicle);

				vehicle.arrivedInQueue(stk.currentTime());
				myAgent().queueBeforeStk.addQueue(myMessage);
			} else  {
				vehicle.arrivedInQueue(stk.currentTime());
				myAgent().queueBeforeStk.addQueue(myMessage);
			}

		} else {
			vehicle.arrivedInQueue(stk.currentTime());
			myAgent().queueBeforeStk.addQueue(myMessage);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.isQueueOpen:
			processIsQueueOpen(message);
		break;

		case Mc.vehicleInspection:
			processVehicleInspection(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processAcceptVehicle:
				processFinishProcessAcceptVehicle(message);
			break;

			case Id.processPayment:
				processFinishProcessPayment(message);
			break;
			}
		break;

		case Mc.vehicleArrivedStk:
			processVehicleArrivedStk(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentGroup1 myAgent()
	{
		return (AgentGroup1)super.myAgent();
	}

}