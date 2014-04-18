package scripts.blastfurnace.jobs;

import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSObjectDefinition;
import org.tribot.api2007.types.RSTile;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox, erickho123
 */
public class PipeRepairer extends Job {

	private final String REPAIR_OPTION = "Repair Pipes";

	@Override
	public boolean shouldDo() {
		RSObject[] pipes = Objects.findNearest(40, Filters.Objects.actionsEquals(REPAIR_OPTION));
		return pipes.length > 0;
	}

	@Override
	public void doJob() {
		RSObject pipe = null;
		RSObject[] pipes = Objects.findNearest(40, Filters.Objects.actionsEquals(REPAIR_OPTION));
		if(pipes.length > 0) {
			pipe = pipes[0];
			if (pipe != null) {
				if (pipe.isOnScreen() && Player.getAnimation() == -1) {
					if (RSUtil.clickRSObject(REPAIR_OPTION, pipe)) {
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								return Player.getAnimation() != -1;
							}
						}, 4000);
					}
				} else {
					if (pipe.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
						Camera.turnToTile(pipe);
					} else {
						Walking.walkTo(pipe.getPosition());
					}
				}
			}
		}
	}
}
