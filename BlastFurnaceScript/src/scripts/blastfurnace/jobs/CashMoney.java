package scripts.blastfurnace.jobs;

import org.tribot.api2007.Game;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Bar;

/**
 * @author Starfox
 */
public class CashMoney extends Job {

	public static Bar barType;
	private final int MAX_PRIMARY_AMOUNT = 28;
	private final int MAX_SECONDARY_AMOUNT = 254;



	@Override
	public boolean shouldDo() {
		return getPrimaryOreAmount() != MAX_PRIMARY_AMOUNT || getSecondaryOreAmount() != MAX_PRIMARY_AMOUNT || barType.getAmountStored() > 0 || needsToCool();
	}

	@Override
	public void doJob() {
		if(needsToCool()) {
			new Cooler().doJob(); // dat laze
		} else if(barType.getAmountStored() > 0) {
			collectBars();
		} else if(getPrimaryOreAmount() != MAX_PRIMARY_AMOUNT) {

		} else if(getSecondaryOreAmount() != MAX_SECONDARY_AMOUNT)  {

		}
	}


	/**
	 * Checks if it needs to cool the dispesner
	 * @return true if it needs to cool the dispenser; returns false if it does not.
	 */
	private boolean needsToCool() {
		return false;
	}
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

	/**
	 * Collects bars from the dispense
	 */
	private void collectBars() {

	}
}
