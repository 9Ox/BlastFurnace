package scripts.blastfurnace.util;

import org.tribot.api.Clicking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;

/**
 * @author Nolan
 */
public enum Bar {
    BRONZE(108), IRON(109), STEEL(110), MITHRIL(111), ADAMANT(112), RUNE(113), SILVER(114), GOLD(115);
    
    private final int MASTER_INDEX = 28;
    private final int childIndex;
    
    Bar(final int childIndex) {
        this.childIndex = childIndex;
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
}
