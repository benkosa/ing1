package CerpaciaStanica.AgentOkolia.AgentModelu.AgentCerpacejStanice;

import CerpaciaStanica.simulacia.Id;
import CerpaciaStanica.simulacia.Mc;
import OSPABA.Agent;
import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;

public class AgentCerpacejStanice extends Agent
{	
	private SimQueue< MessageForm > _frontZakaznikov;
	private Stat _casCakaniaStat;
	private boolean _isWorking;

	public AgentCerpacejStanice(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);

		new ManagerCerpacejStanice(Id.manazerCerpacejStanice, mySim, this);
		
		new ProcesObsluhyZakaznika(Id.procesObsluhyZakaznika, mySim, this);
		
		addOwnMessage(Mc.obsluhaZakaznika);
		addOwnMessage(Mc.koniecObsluhy);
	}
	
	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
	
		_frontZakaznikov = new SimQueue<>(new WStat(mySim()));
		_casCakaniaStat = new Stat();
		_isWorking = false;
	}

	public boolean isWorking()
	{ return _isWorking; }
	
	public void setWorking(boolean isWorking)
	{ _isWorking = isWorking; }
	
	public Stat casCakania()
	{ return _casCakaniaStat; }
	
	public WStat dlzkaFrontu()
	{ return _frontZakaznikov.lengthStatistic(); }
	
	public SimQueue<MessageForm> frontZakaznikov()
	{ return _frontZakaznikov; }
}