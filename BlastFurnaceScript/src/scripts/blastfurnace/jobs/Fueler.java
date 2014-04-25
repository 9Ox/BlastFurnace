package scripts.blastfurnace.jobs;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox, erickho123
 */
public class Fueler extends Job {

    private final int SPADE_ID = 952;
    private final int COKE_ID = 6448;
    private final int HOT_STOVE = 9087;
    private final RSTile STOVE_TILE = new RSTile(1948, 4963, 0);
    private final RSTile COKE_TILE = new RSTile(1950, 4964, 0);
 
    @Override
    public boolean shouldDo() {
        return Objects.findNearest(40, HOT_STOVE).length == 0 || RSUtil.isInCC();
    }

    @Override
    public void doJob() {
        if(RSUtil.isInCC()) {
        	RSUtil.leaveCC();
        } else if (Inventory.getCount(COKE_ID) > 0) {
            refuelStove();
        } else if (Inventory.getCount(SPADE_ID) > 0) {
            collectCoke();
        } else {
            pickupSpade();
        }

    }

    /**
     * Picks up the spade, if it's not on screen it will walk to it.
     */
    private void pickupSpade() {
        RSGroundItem[] spades = GroundItems.findNearest(SPADE_ID);
        if (spades.length > 0) {
            RSGroundItem spade = spades[0];
            if (spade != null) {
                if (spade.isOnScreen()) {
                    if (RSUtil.clickRSGroundItem("Take ", spade)) {
                        Timing.waitCondition(new Condition() {
                            @Override
                            public boolean active() {
                                // TODO Auto-generated method stub
                                return Inventory.getCount(SPADE_ID) > 0;
                            }
                        }, 2000);
                    }

                } else {
                    if (spade.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
                        Camera.turnToTile(spade);
                    } else {
                        Walking.walkTo(spade.getPosition());
                    }
                }
            }
        }
    }

    /**
     * Collects the coke with a spade from a RSObject, will walk to the object if it's not on screen.
     */
    private void collectCoke() {
        RSObject coke = Get.getObject(COKE_TILE);
        if (coke != null) {
            if (coke.isOnScreen()) {
                if (RSUtil.clickRSObject("Collect", coke)) {
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            // TODO Auto-generated method stub
                            return Inventory.getCount(COKE_ID) > 0;
                        }
                    }, 2000);
                }
            } else {
                if (coke.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
                    Camera.turnToTile(coke);
                } else {
                    Walking.walkTo(coke.getPosition());
                }
            }
        }
    }

    /**
     * Heats the stove with the coke, will walk to the object if it's not on screen.
     */
    private void refuelStove() {
        RSObject stove = Get.getObject(STOVE_TILE);
        if (stove != null) {
            if (stove.isOnScreen()) {
                if (RSUtil.clickRSObject("Refuel", stove)) {
                    General.sleep(400, 500);
                }
            } else {
                if (stove.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
                    Camera.turnToTile(stove);
                } else {
                    Walking.walkTo(stove.getPosition());
                }
            }
        }

    }
}
