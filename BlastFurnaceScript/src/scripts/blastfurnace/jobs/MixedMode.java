package scripts.blastfurnace.jobs;

import scripts.blastfurnace.framework.Job;

public class MixedMode extends Job {

	private Fueler fueler;
	private Pedaler pedaler;
	private PipeRepairer repairer;
	private Pumper pumper;
	private ShoutCaller caller;
	private CashMoney smelter;
	public MixedMode(CashMoney smelter, Pedaler pedaler, Fueler fueler, PipeRepairer repairer, Pumper pumper, ShoutCaller caller) {
		this.smelter = smelter;
		this.pedaler = pedaler;
		this.fueler = fueler;
		this.repairer = repairer;
		this.pumper = pumper;
		this.caller = caller;

	}
	@Override
	public boolean shouldDo() {
		return smelter != null && smelter.shouldDo() || pedaler != null && pedaler.shouldDo() ||
				repairer != null && repairer.shouldDo() || pumper != null && pumper.shouldDo() ||
				caller != null && caller.shouldDo();
	}

	@Override
	public void doJob() {
		if(smelter != null && smelter.shouldDo()) {
			smelter.doJob();
		} else if(pedaler != null && pedaler.shouldDo()) {
			pedaler.doJob();
		} else if(fueler != null && fueler.shouldDo()) {
			fueler.shouldDo();
		} else if(repairer != null && repairer.shouldDo()) {
			repairer.shouldDo();
		} else if(caller != null && caller.shouldDo()) {
			caller.shouldDo();
		}

	}

}
