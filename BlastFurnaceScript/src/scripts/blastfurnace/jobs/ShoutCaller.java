package scripts.blastfurnace.jobs;

import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

public class ShoutCaller extends Job {

    private final int PARENT_ID = 30;
    private final int CHILD_ID = 3;
    private final RSTile GAUGE_TILE = new RSTile(1945,4961,0);
    @Override
    public boolean shouldDo() {
        RSInterfaceChild gauge = Interfaces.get(PARENT_ID, CHILD_ID);
        return gauge == null || needsToSayStop();
    }

    @Override
    public void doJob() {
        RSInterfaceChild gauge = Interfaces.get(PARENT_ID, CHILD_ID);
        if (gauge != null) {
            if (needsToSayStop()) {
                Keyboard.typeSend("STOP NIGGA");
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        return !needsToSayStop();
                    }
                }, 20000);
            } else if (!needsToSayStop()) {
                Keyboard.typeSend("START NIGGA");
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        return needsToSayStop();
                    }
                }, 20000);
            }
        } else {
            clickGauge();
        }
    }

    /**
     * Clicks the gauge, will walk to the object if it's not on screen
     */
    private void clickGauge() {
    	RSObject gauge = Get.getObject(GAUGE_TILE);
    	if(gauge != null) {
    		if(gauge.isOnScreen()) {
    			if(RSUtil.clickRSObject("Read", gauge))
    				   Timing.waitCondition(new Condition() {
    	                    @Override
    	                    public boolean active() {
    	                        return Interfaces.get(PARENT_ID, CHILD_ID) != null;
    	                    }
    	                }, 2000);
    		} else {
    			 if (gauge.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
                     Camera.turnToTile(gauge);
                 } else {
                     Walking.walkTo(gauge.getPosition());
                 }
    		}
    	}
    	

    }

    /**
     * Checks the area on the screen for the pointer
     *
     * @return true if the marker is in the red or green area
     */
    private boolean needsToSayStop() {
        return true;
    }
}
