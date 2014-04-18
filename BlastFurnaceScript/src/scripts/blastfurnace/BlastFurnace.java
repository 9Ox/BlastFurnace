package scripts.blastfurnace;

import java.awt.Graphics;

import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;

import scripts.blastfurnace.framework.JobLoop;

/**
 * @author erickho123, starfox
 */
public class BlastFurnace 
        extends Script
        implements Painting {

    /**
     * Creates a new BlastFurnace object.
     * Should be used to initialize any class specific variables and set any internal operations.
     */
    public BlastFurnace() {
        ThreadSettings.get().setClickingAPIUseDynamic(true);
    }
    
    @Override
    public void run() {
        JobLoop.start();
    }

    @Override
    public void onPaint(Graphics g1) {
    }
}
