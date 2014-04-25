package scripts.blastfurnace.util;

import java.util.ArrayList;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

/**
 * @author Starfox
 */
public class Statics {
    public static final RSArea BLAST_FURNACE_AREA = new RSArea(new RSTile(1946, 4963,0), 20); 
    public static String shoutCallerName = "";
    public static boolean startPumping = false;
    public static int startWorld;
    public static ArrayList<String> jobNames = new ArrayList<>();
    public static boolean doJob = false;
    public static String bar = "";
    public static int barCount = 0;
}
 