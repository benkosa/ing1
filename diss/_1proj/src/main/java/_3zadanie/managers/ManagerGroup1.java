package _3zadanie.managers;

import OSPABA.*;
import _2zadanie.Vehicle;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import shared.Workers.Worker;

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

		// code from event simulation

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
				myAgent().averageWaitingBeforeSTK.countAverageTimeInQueue(myMessage.getStartWaitingInQue());
				myAgent().group1.hireWorker(myMessage);

				startProcessAcceptVehicle(myMessage);

				myAgent().averageQueueBeforeSTK.countAverageQueueLength();

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
		final MyMessage myMessage = (MyMessage)message;
		final Vehicle vehicle = myMessage.getVehicle();

		//ak je volny vorker zo skupiny 2 a cakaju auta na inspekciu
		if (myAgent().queueInStk.getReadySize() > 0) {
			final MyMessage newVehicle = myAgent().queueInStk.poll();
			hireWorker(newVehicle);
			startInspection(newVehicle);
		}
		// ak je volny worker zo skupiny 1
		if (myAgent().group1.isWorkerFree()) {
			//ak uz niekto caka v rade
			if (myAgent().queueAfterStk.getSize() > 0) {
				final MyMessage newVehicle = myAgent().queueAfterStk.poll();
				myAgent().group1.hireWorker(newVehicle);
				startProcessPayment(newVehicle);

				vehicle.arrivedInQueue(stk.currentTime());
				myAgent().queueAfterStk.addQueue(myMessage);

			} else  {
				myAgent().group1.hireWorker(myMessage);
				startProcessPayment(myMessage);
			}
		} else {
			vehicle.arrivedInQueue(stk.currentTime());
			myAgent().queueAfterStk.addQueue(myMessage);
		}

	}

	//meta! sender="ProcessAcceptVehicle", id="84", type="Finish"
	public void processFinishProcessAcceptVehicle(MessageForm message)
	{
		isWorkerFree((MyMessage) message);
	}

	//meta! sender="ProcessPayment", id="86", type="Finish"
	public void processFinishProcessPayment(MessageForm message)
	{
		final MyMessage myMessage = (MyMessage)message;
		final Vehicle vehicle = myMessage.getVehicle();

		myAgent().group1.freeWorker(myMessage);
		myMessage.setCode(Mc.vehicleArrivedStk);
		response(myMessage);
		//stk.saveLeftVehicle(vehicle);
		//stk.averageVehiclesInSTK.vehicleLeft();
		//stk.averageVehicleTimeInSystem.vehicleLeft(vehicle);

		// ak niekto caka na platbu a je volny zamestanec zo skupiny 1
		startWorker1Job();
	}

	//meta! sender="AgentStk", id="98", type="Response"
	public void processIsWorkerFree(MessageForm message)
	{
		final MyMessage myMessage = (MyMessage)message;
		final Vehicle vehicle = myMessage.getVehicle();

		vehicle.arrivedInQueue(stk.currentTime());
		myAgent().queueInStk.move(vehicle.id);

		//ak je volny worker zp skupiny 2 a cakaju auta na inspekciu
		if (myMessage.isInspectionWorkerFree()) {
			final MyMessage newVehicle = myAgent().queueInStk.poll();
			startInspection(newVehicle);
		}

		myAgent().group1.freeWorker(myMessage);
		startWorker1Job();

	}


	public void startWorker1Job() {
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

	//meta! sender="AgentStk", id="132", type="Notice"
	public void processFinishedLunchBreak(MessageForm message)
	{
		//worker 2 has finished his lunch break

		if (myAgent().queueInStk.getReadySize() > 0) {
			final MyMessage newVehicle = myAgent().queueInStk.poll();
			startInspection(newVehicle);
		}

		if (myAgent().group1.isWorkerFree()) {
			startWorker1Job();
		}

	}

	//meta! sender="AgentStk", id="151", type="Notice"
	public void processLunchBreakStarted(MessageForm message)
	{
		// lunch break started in group 1
		myAgent().group1.getWorkers().forEach(worker -> {
			MyMessage myMessage = (MyMessage)message.createCopy();
			myMessage.setWorker(worker);
			startLunchBreak(myMessage);
		});

		myAgent().group1.startLunchBreak();
	}

	private void startLunchBreak(MessageForm message) {
		message.setCode(Mc.startLunchBreak);
		message.setAddressee(Id.agentStk);
		request(message);
	}

	//meta! sender="AgentStk", id="156", type="Response"
	public void processStartLunchBreak(MessageForm message)
	{
		// lunch break finished for worker
		MyMessage myMessage = (MyMessage)message;
		Worker worker = myMessage.getWorker();
		myAgent().group1.endLunchBreakWorker(worker);

		// ak niekto caka na platbu a je volny zamestanec zo skupiny 1
		startWorker1Job();
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
		case Mc.isWorkerFree:
			processIsWorkerFree(message);
		break;

		case Mc.finishedLunchBreak:
			processFinishedLunchBreak(message);
		break;

		case Mc.vehicleArrivedStk:
			processVehicleArrivedStk(message);
		break;

		case Mc.lunchBreakStarted:
			processLunchBreakStarted(message);
		break;

		case Mc.vehicleInspection:
			processVehicleInspection(message);
		break;

		case Mc.startLunchBreak:
			processStartLunchBreak(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processPayment:
				processFinishProcessPayment(message);
			break;

			case Id.processAcceptVehicle:
				processFinishProcessAcceptVehicle(message);
			break;
			}
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

	private void isWorkerFree(MyMessage message) {
		message.setCode(Mc.isWorkerFree);
		message.setAddressee(Id.agentStk);
		request(message);
	}

	private void hireWorker(MyMessage message) {
		MessageForm newMessage = message.createCopy();
		MyMessage myMessage = (MyMessage) newMessage;

		myMessage.setVehicle(message.getVehicle());

		myMessage.setAddressee(Id.agentStk);
		myMessage.setCode(Mc.hireWorker);
		notice(myMessage);

	}


}