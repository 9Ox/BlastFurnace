package scripts.blastfurnace.util;

import org.tribot.api.Clicking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

public class RSUtil {

    /**
     * Clicks the RSObject specified with the passed in option.
     * @param option The option to select.
     * @param entities RSObject(s) you want to click.
     * @return returns true if it successfully clicks the object.
     */
    public static boolean clickRSObject(final String option, final RSObject... entities) {
        if (Clicking.click(option, entities)) {
            final RSTile destination = Game.getDestination();
            return destination != null && Player.getPosition().distanceTo(destination) <= 1;
        }
        return false;
    }

    /**
     * Clicks the RSNPC specified with the passed in option
     * @param option The option to select.
     * @param entities RSNPC(s) you want to click.
     * @return returns true if it successfully clicks the npc.
     */
    public static boolean clickRSNPC(final String option, final RSNPC... entities) {
        if (Clicking.click(option, entities)) {
            final RSTile destination = Game.getDestination();
            return destination != null && Player.getPosition().distanceTo(destination) <= 1;
        }
        return false;
    }
}
