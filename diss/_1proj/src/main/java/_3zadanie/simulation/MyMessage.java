package _3zadanie.simulation;

import OSPABA.*;
import _2zadanie.Vehicle;
import shared.Workers.Groupable;
import shared.Workers.Worker;

public class MyMessage extends MessageForm implements Comparable<MyMessage>, Groupable
{
	private Vehicle vehicle;

	private Worker worker;

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	@Override
	public double getStartWaitingInQue() {
		return vehicle.getStartWaitingInQue();
	}

	private boolean isInspectionQueueFree = false;

	public boolean isInspectionWorkerFree() {
		return isInspectionWorkerFree;
	}

	public void setInspectionWorkerFree(boolean inspectionWorkerFree) {
		isInspectionWorkerFree = inspectionWorkerFree;
	}

	private boolean isInspectionWorkerFree = false;

	public void setInspectionQueueFree(boolean inspectionQueueFree) {
		isInspectionQueueFree = inspectionQueueFree;
	}

	public boolean isInspectionQueueFree() {
		return isInspectionQueueFree;
	}

	public MyMessage(Simulation sim)
	{
		super(sim);
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public MyMessage(MyMessage original)
	{
		super(original);
		// copy() is called in superclass
	}

	@Override
	public MessageForm createCopy()
	{
		return new MyMessage(this);
	}

	@Override
	protected void copy(MessageForm message)
	{
		super.copy(message);
		MyMessage original = (MyMessage)message;
		// Copy attributes
	}
	@Override
	public int compareTo(MyMessage o)  {
		return Double.compare(this.getVehicle().getStartWaitingInQue(), o.getVehicle().getStartWaitingInQue());
	}

	@Override
	public long getId() {
		return vehicle.getId();
	}
}