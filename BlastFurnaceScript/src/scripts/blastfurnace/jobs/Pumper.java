package scripts.blastfurnace.jobs;

import org.tribot.api2007.Game;
import org.tribot.api2007.Player;

import scripts.blastfurnace.framework.Job;

/**
 * @author Starfox, erickho123
 */ 
public class Pumper extends Job {
	
	private final int PUMPING_ANIMATION = 2432;
	private final int TEMPEATURE_SETTING = -1;
	
	@Override
	public boolean shouldDo() {
		// place holder, not actual setting or temp
		return Game.getSetting(TEMPEATURE_SETTING) <= 20;
	}

	@Override
	public void doJob() {
		if(Player.getAnimation() == PUMPING_ANIMATION) {
			// sleep 
		} else {
			//RSObject pump = Get.
		}
		
	}

}
