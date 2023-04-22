package _3zadanie.simulation;

import OSPABA.*;
import _2zadanie.Vehicle;

public class MyMessage extends MessageForm
{
	private Vehicle vehicle;
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
}