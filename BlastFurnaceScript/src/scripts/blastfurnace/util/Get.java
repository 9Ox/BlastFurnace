package scripts.blastfurnace.util;

import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

/**
 * @author Starfox
 */
public class Get {
    /**
     * Returns the nearest RSObject with the specified id(s) and within the specified distance.
     * @param dist Distance to stay within.
     * @param ids The id(s) to check for.
     * @return The nearest RSObject found; null if none were found.
     */
    public static RSObject get(final int dist, final int... ids) {
        RSObject[] objs = Objects.findNearest(dist, ids);
        return objs.length > 0 ? objs[0] : null;
    }
    
    /**
     * Returns the nearest RSNPC with the specified id(s).
     * @param ids The id(s) to check for.
     * @return The nearest RSNPC found; null if none were found.
     */
    public static RSNPC get(final int... ids) {
        RSNPC[] npcs = NPCs.find(ids);
        return npcs.length > 0 ? npcs[0] : null;
    }
}