package scripts.blastfurnace.jobs;

import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSObject;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox, erickho123
 */
public class PipeRepairer extends Job {

    private final String REPAIR_OPTION = "Repair";

    @Override
    public boolean shouldDo() {
        RSObject[] pipes = Objects.findNearest(40, Filters.Objects.actionsContains(REPAIR_OPTION));
        return pipes.length > 0;
    }

    @Override
    public void doJob() {
    	Interfaces.closeAll();
        RSObject[] pipes = Objects.findNearest(40, Filters.Objects.actionsContains(REPAIR_OPTION));
        RSInterfaceChild gauge = Interfaces.get(30,3);
        if (pipes.length > 0) {
            RSObject pipe = pipes[0];
            if (pipe != null) {
            	if(gauge != null) {
            		Walking.walkTo(pipe.getPosition());
            	} else  if (pipe.isOnScreen() && Player.getAnimation() == -1) {
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
