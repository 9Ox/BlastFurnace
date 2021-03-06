package scripts.blastfurnace.jobs;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.framework.JobManager;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Statics;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox, erickho123
 */
public class Pumper extends Job {

	private final int PUMPING_ANIMATION = 2432;
	private final int TEMPEATURE_SETTING = -1;
	private final RSTile STOP_TILE = new RSTile(1952, 4961, 0);
	private final RSTile PUMP_TILE = new RSTile(1950, 4961, 0);
	private final RSTile SHOUT_CALLER_TILE = new RSTile(1945,4960,0);
	private Fueler fueler;
	public Pumper() {
		fueler = new Fueler();
	}

	@Override
	public boolean shouldDo() {
		int JobSize = JobManager.getJobs().size();
		return JobSize == 1 && (Get.getPlayer(SHOUT_CALLER_TILE) == null || Statics.startPumping && Player.getAnimation() != PUMPING_ANIMATION || !Statics.startPumping && Player.getAnimation() == PUMPING_ANIMATION || 
				Combat.getHPRatio() <= 70 || !RSUtil.isInCC()) || JobManager.getJobs().size() > 1 && !fueler.shouldDo();
	}

	@Override
	public void doJob() {
		Interfaces.closeAll();
		int playerAnimation = Player.getAnimation();
		if (!RSUtil.isInCC()) {
			RSUtil.joinCC(Statics.shoutCallerName);
		} else if (!Statics.startPumping && playerAnimation == PUMPING_ANIMATION || Combat.getHPRatio() <= 70 || Get.getPlayer(SHOUT_CALLER_TILE) == null) {
			if (Player.getPosition().distanceTo(STOP_TILE) != 0) {
				Walking.walkPath(true, STOP_TILE);
			} 
		} else if (Statics.startPumping && playerAnimation == PUMPING_ANIMATION) {
			General.sleep(10, 20); // condtional sleep maybe later

		} else {
			operatePump();
		}
	}

	/**
	 * Operates the pump when needed, when shout caller calls "Stop", it will run to a safe tile
	 */
	private void operatePump() {
		RSObject pump = Get.getObject(PUMP_TILE);
		if (pump != null) {
			if (pump.isOnScreen()) {
				if (RSUtil.clickRSObject("Pump", pump)) {
					if (Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							return Player.getAnimation() == PUMPING_ANIMATION;
						}
					}, 5000)) {
						Timing.waitCondition(new Condition() {

							@Override
							public boolean active() {
								return !Statics.startPumping || Combat.getHPRatio() <= 70;
							}
						}, 5000);
					}
				}
			} else {
				if (pump.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
					Camera.turnToTile(pump);
				} else {
					Walking.blindWalkTo(pump.getPosition());
				}
			}
		}
	}

}
