package scripts.blastfurnace.jobs;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox, erickho123
 */
public class Pedaler extends Job {

	private final int PEDALING_ANIMATION = 2433;
	private final String[] ORE_NAMES = {"Iron ore", "Coal", "Adamantite ore", "Runite ore"};
	private final RSTile WALK_TILE = new RSTile(1949, 4966, 0);
	private final RSTile PEDALS_TILE = new RSTile(1947,4966,0);
	@Override
	public boolean shouldDo() {
		RSNPC[] ores = NPCs.findNearest(ORE_NAMES);
		return ores.length > 0 || ores.length == 0 && Player.getAnimation() == PEDALING_ANIMATION;
	}

	@Override
	public void doJob() {
		RSNPC[] ores = NPCs.findNearest(ORE_NAMES);
		int playerAniamtion = Player.getAnimation();
		if (ores.length == 0 && playerAniamtion == PEDALING_ANIMATION) {
			Walking.walkTo(WALK_TILE);
		} else if (ores.length > 0) {
			if (playerAniamtion == PEDALING_ANIMATION) {
				General.sleep(20, 30); // condtional sleep maybe later
			} else {
				pedalPedals();
			}
		}
	}

	/**
	 * Pedals the pedals so that the ores can be inserted
	 */
	private void pedalPedals() {
		RSObject pedal = Get.getObject(PEDALS_TILE);
		if (pedal != null) {
			if (pedal.isOnScreen() && Player.getAnimation() == -1) {
				if (RSUtil.clickRSObject("Pedal", pedal)) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return Player.getAnimation() != -1;
						}
					}, 4000);
				}
			} else {
				if (pedal.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
					Camera.turnToTile(pedal);
				} else {
					Walking.walkTo(pedal.getPosition());
				}
			}
		}
	}
}
