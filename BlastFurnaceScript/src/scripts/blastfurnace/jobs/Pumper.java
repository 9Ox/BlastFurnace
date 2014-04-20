package scripts.blastfurnace.jobs;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.RSUtil;

/**
 * @author Starfox, erickho123
 */
public class Pumper extends Job {

    public static boolean startPumping = false;
    private final int PUMPING_ANIMATION = 2432;
    private final int TEMPEATURE_SETTING = -1;
    private final RSTile STOP_TILE = new RSTile(1952, 4961, 0);
    
    public Pumper() {
    }

    @Override
    public boolean shouldDo() {
        return startPumping && Player.getAnimation() != PUMPING_ANIMATION || !startPumping && Player.getAnimation() == PUMPING_ANIMATION || Combat.getHPRatio() <= 70;
    }

    @Override
    public void doJob() {
        int playerAnimation = Player.getAnimation();
        if (startPumping && playerAnimation == PUMPING_ANIMATION) {
            General.sleep(10, 20); // condtional sleep maybe later
        } else if (!startPumping && playerAnimation == PUMPING_ANIMATION || Combat.getHPRatio() <= 70) {
            Walking.walkTo(STOP_TILE);
        } else {
            operatePump();
        }
    }

    /**
     * Operates the pump when needed, when shout caller calls "Stop", it will run to a safe tile
     */
    private void operatePump() {
        RSObject[] pumps = Objects.findNearest(40, "Pumps");
        if (pumps.length > 0) {
            RSObject pump = pumps[0];
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
                                    return !startPumping || Combat.getHPRatio() <= 70;
                                }
                            }, 5000);
                        }
                    }
                } else {
                    if (pump.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
                        Camera.turnToTile(pump);
                    } else {
                        Walking.walkTo(pump.getPosition());
                    }
                }
            }
        }
    }

}
