package scripts.blastfurnace.jobs;

import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;

import scripts.blastfurnace.framework.Job;

public class ShoutCaller extends Job {

    private final int PARENT_ID = 30;
    private final int CHILD_ID = 3;

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
    public static void clickGauge() {

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
