package scripts.blastfurnace.util;

import java.awt.Color;
import java.awt.Point;
import org.tribot.api.Clicking;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Screen;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

public class RSUtil {

	/**
	 * Clicks the RSObject specified with the passed in option.
	 *
	 * @param option The option to select.
	 * @param entities RSObject(s) you want to click.
	 * @return returns true if it successfully clicks the object.
	 */
	public static boolean clickRSObject(final String option, final RSObject... entities) {
		if (Clicking.click(option, entities)) {
			final RSTile destination = Game.getDestination();
			return destination != null && entities[0].getPosition().distanceTo(destination) <= 0;
		}
		return false;
	} 

	/**
	 * Clicks the RSNPC specified with the passed in option
	 *
	 * @param option The option to select.
	 * @param entities RSNPC(s) you want to click.
	 * @return returns true if it successfully clicks the npc.
	 */
	public static boolean clickRSNPC(final String option, final RSNPC... entities) {
		if (Clicking.click(option, entities)) {
			final RSTile destination = Game.getDestination();
			return destination != null && entities[0].getPosition().distanceTo(destination) <= 0;
		}
		return false;
	}

	/**
	 * Clicks the RSGroundItem specified with the passed in option
	 *
	 * @param option The option to select.
	 * @param entities RSGroundItem(s) you want to click.
	 * @return returns true if it successfully clicks the RSGroundItem.
	 */
	public static boolean clickRSGroundItem(final String option, final RSGroundItem... entities) {
		if (Clicking.click(option, entities)) {
			final RSTile destination = Game.getDestination();
			return destination != null && entities[0].getPosition().distanceTo(destination) <= 0;
		}
		return false;
	}

	/**
	 * Checks to see if you're in a cc.
	 *
	 * @return true if in a cc; false otherwise.
	 */
	public static boolean isInCC() {
		final RSInterfaceChild child = Interfaces.get(589, 1);
		if (child != null) {
			String text = child.getText();
			return text != null && !text.contains("None");
		}
		return false;
	}

	/**
	 * Joins the specified cc.
	 *
	 * @param name The name of the cc.
	 * @return true if successful; false otherwise.
	 */
	public static boolean joinCC(final String name) {
		if (isInCC()) {
			return true;
		}
		if (RSUtil.isEnterAmountUp()) {
			Keyboard.typeSend(name);
			return Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					return isInCC();
				}
			}, 3000);
		}
		if (!GameTab.TABS.CLAN.isOpen()) {
			Keyboard.pressFunctionKey(7);
		}
		if (GameTab.TABS.CLAN.isOpen()) {
			final RSInterfaceChild button = Interfaces.get(589, 2);
			if (button != null && button.click()) {
				if(Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return RSUtil.isEnterAmountUp();
					}
				}, 2500)) {
					Keyboard.typeSend(name);
					return Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return isInCC();
						}
					}, 3000);
				}
			}
		}
		return false;
	}
	
	/**
	 * Joins the specified cc.
	 **/
	public static boolean leaveCC() {
		if (!isInCC()) {
			return true;
		}

		if (!GameTab.TABS.CLAN.isOpen()) {
			Keyboard.pressFunctionKey(7);
		}
		if (GameTab.TABS.CLAN.isOpen()) {
			final RSInterfaceChild button = Interfaces.get(589, 8);
			if (button != null && button.click()) {
				return Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return !isInCC();
					}
				}, 4000);
			}

		}
		return false;
	}

	/**
	 * Checks to see whether or not the enter amount menu is visible.
	 *
	 * @return true if it's visible; false otherwise.
	 */
	public static boolean isEnterAmountUp() {
		return Screen.getColorAt(new Point(260, 428)).equals(new Color(0, 0, 128));
	}
}
