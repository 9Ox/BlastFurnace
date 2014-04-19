package scripts.blastfurnace.util;

import org.tribot.api.Clicking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;

/**
 * @author Nolan
 */
public enum Bar {
    BRONZE(108, -1), IRON(109, -1), STEEL(110, -1), MITHRIL(111, -1), ADAMANT(112, -1), RUNE(113, -1), SILVER(114, -1), GOLD(115, -1);
    
    private final int MASTER_INDEX = 28;
    private final int SETTING_INDEX = 545;
    private final int childIndex;
    private final int bits;
    Bar(final int childIndex, final int bits) {
        this.childIndex = childIndex;
        this.bits = bits;
    }
    
    private int getChildIndex() {
        return this.childIndex;
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
}
