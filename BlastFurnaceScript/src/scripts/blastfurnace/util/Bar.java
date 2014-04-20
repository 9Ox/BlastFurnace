package scripts.blastfurnace.util;

import org.tribot.api.Clicking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;

/**
 * @author Nolan
 */
public enum Bar {
    BRONZE(108, 0, new int[]{436, 438}), IRON(109, 8, new int[]{440}), STEEL(110, 16, new int[]{440, 453}), MITHRIL(111, 24, new int[]{447, 453}), 
    ADAMANT(112, 32, new int[]{449, 453}), RUNE(113, 40, new int[]{451, 453}), SILVER(114, 48, new int[]{442}), GOLD(115, 56, new int[]{444});
    
    private final int MASTER_INDEX = 28;
    private final int SETTING_INDEX = 545;
    private final int childIndex;
    private final int bits;
    private final int[] ores;
    
    Bar(final int childIndex, final int bits, int[] ores) {
        this.childIndex = childIndex;
        this.bits = bits;
        this.ores = ores;
    }
    
    private int getChildIndex() {
        return this.childIndex;
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
