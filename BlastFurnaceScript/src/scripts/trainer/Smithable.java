package scripts.trainer;

import java.awt.Color;
import java.awt.Point;
import org.tribot.api.Clicking;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Screen;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;

/**
 * @author Nolan 
 */
public enum Smithable {
    BRONZE_DAGGER(1, 2, 2349, 1), BRONZE_SCIMITAR(5, 4, 2349, 2), BRONZE_WARHAMMER(9, 9, 2349, 3), BRONZE_PLATE(18, 15, 2349, 5),
    STEEL_DAGGER(30, 2, 2353, 1), STEEL_SCIMITAR(35, 4, 2353, 2), STEEL_WARHAMMER(39, 9, 2353, 3), STEEL_PLATE(48, 15, 2353, 5);
    
    private final int level;
    private final int childIndex;
    private final int barId;
    private final int numBars;
    
    Smithable(final int level, final int childIndex, final int barId, final int numBars) {
        this.level = level;
        this.childIndex = childIndex;
        this.barId = barId;
        this.numBars = numBars;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getBarId() {
        return this.barId;
    }
    
    public int getNumBars() {
        return this.numBars;
    }
    
    private int getChildIndex() {
        return this.childIndex;
    }
    
    public RSInterfaceComponent getComponent() {
        final RSInterfaceChild child = Interfaces.get(312, getChildIndex());
        return child != null ? child.getChild(2) : null;
    }
    
    public boolean clickComponent() {
        final RSInterfaceComponent component = getComponent();
        if (component == null) {
            return false;
        }
        if (name().contains("WAR") || name().contains("PLATE")) {
            return Clicking.click("Smith 10", component);
        } else {
            if (Clicking.click("Smith X", component)) {
                return Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        return isEnterAmountUp();
                    }
                }, 3000);
            }
        }
        return false;
    }
    
    /**
     * Checks to see whether or not the enter amount menu is visible.
     * @return true if it's visible; false otherwise.
     */
    public static boolean isEnterAmountUp() {
        return Screen.getColorAt(new Point(260, 428)).equals(new Color(0, 0, 128));
    }
}
