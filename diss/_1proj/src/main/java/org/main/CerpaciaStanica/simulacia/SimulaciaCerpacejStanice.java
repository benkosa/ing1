package org.main.CerpaciaStanica.simulacia;

import org.main.CerpaciaStanica.agenti.AgentCerpacejStanice;
import org.main.CerpaciaStanica.agenti.AgentModelu;
import org.main.CerpaciaStanica.agenti.AgentOkolia;
import OSPABA.Simulation;
import OSPStat.Stat;

public class SimulaciaCerpacejStanice extends Simulation
{
	private AgentModelu _agentModelu;
	private AgentOkolia _agentOkolia;
	private AgentCerpacejStanice _agentCerpacejStanice;
	
	private Stat _casCakaniaStat;

	public SimulaciaCerpacejStanice()
	{
		_agentModelu = new AgentModelu(Id.agentModelu, this, null);
		_agentOkolia = new AgentOkolia(Id.agentOkolia, this, _agentModelu);
		_agentCerpacejStanice = new AgentCerpacejStanice(Id.agentCerpacejStanice, this, _agentModelu);
	}
	
	@Override
	protected void prepareSimulation()
	{
		super.prepareSimulation();
		_casCakaniaStat = new Stat();
	}

	@Override
	protected void prepareReplication()
	{
		super.prepareReplication();
		agentModelu().spustiSimulaciu();
	}

	@Override
	protected void replicationFinished()
	{
		super.replicationFinished();
		_casCakaniaStat.addSample(agentCerpacejStanice().casCakania().mean());
		System.out.println("R"+ currentReplication() +": "+  _casCakaniaStat.mean() +"("+agentCerpacejStanice().casCakania().mean()+")");
	}

	@Override
	protected void simulationFinished()
	{
		super.simulationFinished();
		System.out.println("Waiting time mean:  "+ _casCakaniaStat.mean());
	}
	
	public AgentModelu agentModelu()
	{ return _agentModelu; }
	
	public AgentOkolia agentOkolia()
	{ return _agentOkolia; }
	
	public AgentCerpacejStanice agentCerpacejStanice()
	{ return _agentCerpacejStanice; }
}
