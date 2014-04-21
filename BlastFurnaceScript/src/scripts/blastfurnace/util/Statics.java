package scripts.blastfurnace.util;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

/**
 * @author Starfox
 */
public class Statics {
    public static final RSArea BLAST_FURNACE_AREA = new RSArea(new RSTile(1935, 4974, 0), new RSTile(1957 , 4956, 0)); 
    public static String shoutCallerName = "";
    public static boolean startPumping = false;
    public static int startWorld;
    public static String jobName = "";
    public static boolean doJob = false;
    public static String bar = "";
    public static int barCount = 0;
}
