package scripts.blastfurnace.jobs;

import org.tribot.api2007.Game;

import scripts.blastfurnace.framework.Job;

/**
 * @author Starfox
 */
public class CashMoney extends Job {

	/**
	 * Gets the amount of your primary ore in the furnace
	 * @return the main ore amount
	 */
	private int getPrimaryOreAmount() {
		return (Game.getSetting(547) >> 24);
	}
	
	/**
	 * Gets the amount of your secondary ore in the furnace
	 * @return the secondary
	 */
	private int getSecondaryOreAmount() {
		return (Game.getSetting(547)) & 0xFE;
	}
    @Override
    public boolean shouldDo() {
        return true;
    }

    @Override
    public void doJob() {
    }

}
