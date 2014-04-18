package scripts.blastfurnace.jobs;

import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox, erickho123
 */
public class PipeRepairer extends Job {

    private final int[] BROKEN_PIPE_IDS = {9121};
    
    @Override
    public boolean shouldDo() {
        return Objects.findNearest(40, BROKEN_PIPE_IDS).length > 0;
    }

    @Override
    public void doJob() {
	RSObject pipe = Get.get(40, BROKEN_PIPE_IDS);
        if (pipe != null) {
            if (pipe.isOnScreen() && Player.getAnimation() == -1) {
                if (RSUtil.clickRSObject("Repair Pipes", pipe)) {
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
