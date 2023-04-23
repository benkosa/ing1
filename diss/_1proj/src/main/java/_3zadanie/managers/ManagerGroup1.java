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
		final MyMessage myMessage = (MyMessage)message;
		final Vehicle vehicle = myMessage.getVehicle();

		System.out.println("vehicle arrived: " + myMessage.getVehicle().id + " : " + message.deliveryTime());

		System.out.println(myAgent().group1.isWorkerFree());

		// code from event simulation
		myMessage.getVehicle().setArrived(stk.currentTime());

		// je volny worker
		if (myAgent().group1.isWorkerFree()) {
			//niekto caka na platbu
			if (myAgent().queueAfterStk.getSize() > 0) {
				final MyMessage newVehicle = myAgent().queueAfterStk.poll();
				myAgent().group1.hireWorker(newVehicle);
				startProcessPayment(newVehicle);
				// ideme priamo na receive
			} else if (myAgent().queueInStk.isSpaceInQueue() && myAgent().queueBeforeStk.getSize() == 0){
				vehicle.arrivedInQueue(stk.currentTime());
				//stk.averageWaitingBeforeSTK.countAverageTimeInQueue(vehicle.startWaitingInQue);
				myAgent().group1.hireWorker(myMessage);

				startProcessAcceptVehicle(myMessage);

				//stk.averageQueueBeforeSTK.countAverageQueueLength();

			}else if (myAgent().queueInStk.isSpaceInQueue() && myAgent().queueBeforeStk.getSize() > 0) {
				final MyMessage newVehicle = myAgent().queueBeforeStk.poll();
				myAgent().group1.hireWorker(newVehicle);

				startProcessAcceptVehicle(newVehicle);

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

	//meta! sender="AgentStk", id="69", type="Response"
	public void processVehicleInspection(MessageForm message)
	{
		System.out.println("RETURNED FROM INSPECTION");
	}

	//meta! sender="ProcessAcceptVehicle", id="84", type="Finish"
	public void processFinishProcessAcceptVehicle(MessageForm message)
	{
		message.setCode(Mc.isWorkerFree);
		message.setAddressee(Id.agentStk);
		request(message);
	}

	//meta! sender="ProcessPayment", id="86", type="Finish"
	public void processFinishProcessPayment(MessageForm message)
	{
	}

	//meta! sender="AgentStk", id="98", type="Response"
	public void processIsWorkerFree(MessageForm message)
	{
		final MyMessage myMessage = (MyMessage)message;
		final Vehicle vehicle = myMessage.getVehicle();
		System.out.println("accept vehicle finished");

		vehicle.arrivedInQueue(stk.currentTime());
		myAgent().queueInStk.move(vehicle.id);

		//ak je volny worker zp skupiny 2 a cakaju auta na inspekciu
		if (myMessage.isInspectionWorkerFree()) {
			final MyMessage newVehicle = myAgent().queueInStk.poll();
			startInspection(newVehicle);
		}

		myAgent().group1.freeWorker(myMessage);
		// ak niekto caka na platbu a je volny zamestanec zo skupiny 1
		if (myAgent().queueAfterStk.getSize() > 0) {
			final MyMessage newVehicle = myAgent().queueAfterStk.poll();
			myAgent().group1.hireWorker(newVehicle);
			startProcessPayment(newVehicle);
			// ak niekto caka pred stk a je volny zamestnace zo skupiny 1 a je volne miesto na parkovisku
		} else if (
				myAgent().queueBeforeStk.getSize() > 0 &&
						myAgent().queueInStk.isSpaceInQueue()
		) {
			final MyMessage newVehicle = myAgent().queueBeforeStk.poll();
			myAgent().group1.hireWorker(newVehicle);
			startProcessAcceptVehicle(newVehicle);
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
		case Mc.vehicleInspection:
			processVehicleInspection(message);
		break;

		case Mc.isWorkerFree:
			processIsWorkerFree(message);
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

	private void startProcessAcceptVehicle(MyMessage message) {
		final Vehicle vehicle = message.getVehicle();

		vehicle.arrivedInQueue(stk.currentTime());
		myAgent().queueInStk.addQueueLocked(message.getId(), message);

		message.setAddressee(myAgent().findAssistant(Id.processAcceptVehicle));
		startContinualAssistant(message);

	}

	private void startProcessPayment(MyMessage message) {

		message.setAddressee(myAgent().findAssistant(Id.processPayment));
		startContinualAssistant(message);

	}

	private void startInspection(MyMessage message) {

		message.setCode(Mc.vehicleInspection);
		message.setAddressee(Id.agentStk);
		request(message);

	}


}