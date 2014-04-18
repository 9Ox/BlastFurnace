package scripts.blastfurnace.util;

import org.tribot.api.Clicking;
import org.tribot.api.interfaces.Clickable;
import org.tribot.api2007.Game;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

public class RSUtil {

	/**
	 * @param entity RSObject you desire to click
	 * @return returns true if it successfully clicks the object.
	 */
	public static boolean clickRSObject(final RSObject entity) {
		if(Clicking.click(entity)) {
			final RSTile destination = Game.getDestination();
			return destination != null && Player.getPosition().distanceTo(destination) <= 1;
		}
		return false;
	}
	
	/**
	 * @param entity RSNPC you desire to click
	 * @return returns true if it successfully clicks the npc.
	 */
	public static boolean clickRSNPC(final RSNPC entity) {
		if(Clicking.click(entity)) {
			final RSTile destination = Game.getDestination();
			return destination != null && Player.getPosition().distanceTo(destination) <= 1;
		}
		return false;
	}
}
