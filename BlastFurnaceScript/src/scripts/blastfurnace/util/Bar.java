package scripts.blastfurnace.util;

import org.tribot.api.Clicking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;

/**
 * @author Nolan
 */
public enum Bar {
    BRONZE(2349, 108, 0, new int[]{436, 438}), IRON(2351, 109, 8, new int[]{440}), STEEL(2353, 110, 16, new int[]{440, 453}), MITHRIL(2359, 111, 24, new int[]{447, 453}), 
    ADAMANT(2361, 112, 32, new int[]{449, 453}), RUNE(2363, 113, 40, new int[]{451, 453}), SILVER(2355, 114, 48, new int[]{442}), GOLD(2357, 115, 56, new int[]{444});
    
    private final int id;
    private final int MASTER_INDEX = 28;
    private final int SETTING_INDEX = 545;
    private final int childIndex;
    private final int bits;
    private final int[] ores;
    
    Bar(final int id, final int childIndex, final int bits, int[] ores) {
        this.id = id;
    	this.childIndex = childIndex;
        this.bits = bits;
        this.ores = ores;
    }
    
    private int getChildIndex() {
        return this.childIndex;
    }
    
    public int getID() {
    	return id;
    }
    
    public int[] getOres() {
        return this.ores;
    }
    
    public RSInterfaceChild getInterface() {
        return Interfaces.get(MASTER_INDEX, getChildIndex());
    }
    
    public boolean clickInterface(String option) {
        RSInterfaceChild child = getInterface();
        return child != null && Clicking.click(option, child);
    }
    
    public int getAmountStored() {
    	return Game.getSetting(SETTING_INDEX) >> bits;
    }
    
    public boolean requiresSeconary() {
        return getOres().length > 1;
    }
}
