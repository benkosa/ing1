package _3zadanie.managers;

import OSPABA.*;
import _2zadanie.Vehicle;
import _3zadanie.simulation.*;
import _3zadanie.agents.*;
import _3zadanie.continualAssistants.*;
import _3zadanie.instantAssistants.*;
import shared.Workers.Worker;

//meta! id="59"
public class ManagerInspection extends Manager
{
	public ManagerInspection(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
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

	//meta! sender="AgentStk", id="74", type="Request"
	public void processVehicleInspection(MessageForm message)
	{
		// start inspection
		message.setAddressee(myAgent().findAssistant(Id.processInspection));
		startContinualAssistant(message);

	}

	//meta! sender="ProcessInspection", id="89", type="Finish"
	public void processFinish(MessageForm message)
	{
		MyMessage message1 = (MyMessage) message;
		Worker worker = myAgent().group2.getHiredWorkers().get(message1.getId());
		myAgent().group2.freeWorker(message1);

		if (myAgent().group2.isLunchBreakTime() && worker.shouldGoToLunchBreak()) {
			MyMessage lunchBreakMessage = (MyMessage)message.createCopy();
			lunchBreakMessage.setWorker(worker);
			startLunchBreak(lunchBreakMessage);
			message1.setWorkerStartedLunchBreak(true);
		}

		message.setCode(Mc.vehicleInspection);
		response(message);
	}

	//meta! sender="AgentStk", id="102", type="Request"
	public void processIsWorkerFree(MessageForm message)
	{
		MyMessage message1 = (MyMessage) message;
		message1.setInspectionWorkerFree(myAgent().group2.isWorkerFree());
		if (myAgent().group2.isWorkerFree()) {
			myAgent().group2.hireWorker(message1);
		}
		response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentStk", id="119", type="Notice"
	public void processHireWorker(MessageForm message)
	{
		myAgent().group2.hireWorker((MyMessage) message);

	}

	//meta! sender="AgentStk", id="152", type="Notice"
	public void processLunchBreakStarted(MessageForm message)
	{
		//System.out.println("lunch break started in group 1 sdfd");
		myAgent().group2.getWorkers().forEach(worker -> {
			MyMessage myMessage = (MyMessage)message.createCopy();
			myMessage.setWorker(worker);
			startLunchBreak(myMessage);
		});

		myAgent().group2.startLunchBreak();
	}

	private void startLunchBreak(MessageForm message) {
		message.setCode(Mc.startLunchBreak);
		message.setAddressee(Id.agentStk);
		request(message);
	}

	//meta! sender="AgentStk", id="158", type="Response"
	public void processStartLunchBreak(MessageForm message)
	{
		MyMessage myMessage = (MyMessage)message;
		Worker worker = myMessage.getWorker();
		myAgent().group2.endLunchBreakWorker(worker);

		message.setCode(Mc.finishedLunchBreak);
		message.setAddressee(Id.agentStk);
		notice(message);

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

		case Mc.hireWorker:
			processHireWorker(message);
		break;

		case Mc.lunchBreakStarted:
			processLunchBreakStarted(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.isWorkerFree:
			processIsWorkerFree(message);
		break;

		case Mc.startLunchBreak:
			processStartLunchBreak(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentInspection myAgent()
	{
		return (AgentInspection)super.myAgent();
	}

}