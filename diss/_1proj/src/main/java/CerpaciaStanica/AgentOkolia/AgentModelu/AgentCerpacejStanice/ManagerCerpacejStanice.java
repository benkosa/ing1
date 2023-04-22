package CerpaciaStanica.AgentOkolia.AgentModelu.AgentCerpacejStanice;

import CerpaciaStanica.simulacia.Id;
import CerpaciaStanica.simulacia.Mc;
import CerpaciaStanica.simulacia.Sprava;
import OSPABA.Agent;
import OSPABA.Manager;
import OSPABA.MessageForm;
import OSPABA.Simulation;

public class ManagerCerpacejStanice extends Manager
{
	public ManagerCerpacejStanice(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);		
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.obsluhaZakaznika:
			// ak je novinovy stanok zaneprazdneny zacneme cakat v rade
			if (myAgent().isWorking())
			{
				((Sprava)message).setZaciatokCakania(mySim().currentTime());
				myAgent().frontZakaznikov().enqueue(message);
			}
			//stanok zacne pracovat
			else
			{
				startWork(message);
			}
		break;

		//ukonecnie obslhy
		case Mc.finish: // procesObsluhyZakaznika
			myAgent().setWorking(false);
			myAgent().casCakania().addSample(((Sprava)message).celkoveCakanie());

			//ak je niekto vo fronte zacneme pracovat
			if (0 < myAgent().frontZakaznikov().size())
			{
				Sprava nextMessage = (Sprava)myAgent().frontZakaznikov().dequeue();
				nextMessage.setCelkoveCakanie(mySim().currentTime() - nextMessage.zaciatokCakania());
				startWork(nextMessage);
			}

			//koniec obsluhy pre povodenho zakaznika
			message.setCode(Mc.obsluhaZakaznikaHotova);
			response(message);
		break;
		}		
	}
	
	private void startWork(MessageForm message)
	{
		myAgent().setWorking(true);
//		message.setAddressee(myAgent().procesObsluhyZakaznika());
		message.setAddressee(myAgent().findAssistant(Id.procesObsluhyZakaznika));
		startContinualAssistant(message);		
	}

	@Override
	public AgentCerpacejStanice myAgent()
	{ return (AgentCerpacejStanice)(super.myAgent()); }
}
