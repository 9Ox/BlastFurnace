package scripts.blastfurnace;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.MousePainting;
import org.tribot.script.interfaces.MouseSplinePainting;
import org.tribot.script.interfaces.Painting;
import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.framework.JobLoop;
import scripts.blastfurnace.framework.JobManager;
import scripts.blastfurnace.jobs.CashMoney;
import scripts.blastfurnace.jobs.Cooler;
import scripts.blastfurnace.jobs.Fueler;
import scripts.blastfurnace.jobs.Pedaler;
import scripts.blastfurnace.jobs.PipeRepairer;
import scripts.blastfurnace.jobs.Pumper;
import scripts.blastfurnace.jobs.ShoutCaller;
import scripts.blastfurnace.paint.Paint;
import scripts.blastfurnace.util.Bar;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.Statics;
import scripts.blastfurnace.util.WorldHop;

/**
 * @author erickho123, starfox
 */
public class BlastFurnace
        extends Script
        implements Painting, Arguments, MessageListening07, MousePainting, MouseSplinePainting, EventBlockingOverride {

    /**
     * Creates a new BlastFurnace object. Should be used to initialize any class specific variables and set any internal operations.
     */
    public static BlastFurnace script;
    public static final int START_WORLD = WorldHop.getWorld();
    private final Rectangle PAINT_RECT;
    private boolean showPaint;
    private float opacity = 1.0f;

    public BlastFurnace() {
        PAINT_RECT = new Rectangle(7, 345, 490, 129);
        showPaint = true;
        JobLoop.setReady(true);
    }

    @Override
    public void run() {
        script = this;
        JobLoop.start();
    }

    @Override
    public void onPaint(Graphics g1) {
        Graphics2D g2 = (Graphics2D) g1;
        if (!showPaint && opacity > 0.0f) {
            opacity -= .05f;
        } else if (showPaint && opacity < 1.0f) {
            opacity += .05f;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        ((Graphics2D) g1).setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRect(PAINT_RECT.x, PAINT_RECT.y, PAINT_RECT.width, PAINT_RECT.height);
        g2.setColor(new Color(255, 255, 255, 220));
        g2.drawString("The swag furnace v1.0", 17, 365);
    }

    @Override
    public void passArguments(HashMap<String, String> arg0) {
        String input = arg0.get("custom_input");
        if (input != null) {
            switch (input) {
                case "Fueler":
                    JobManager.addJob(new Fueler());
                    break;
                case "Pedaler":
                    JobManager.addJob(new Pedaler());
                    break;
                case "PipeRepairer":
                    JobManager.addJob(new PipeRepairer());
                    break;
                case "CashMoney":
                    Bar barType = Get.getBar(input.split(":")[1]);
                    JobManager.addJob(new CashMoney(barType));
                    break;
                case "ShoutCaller":
                    JobManager.addJob(new ShoutCaller());
                    break;
                case "Cooler":
                    JobManager.addJob(new Cooler());
                    break;
                case "Pumper":
                    Statics.shoutCallerName = input.split(":")[1];
                    JobManager.addJob(new Pumper());
                default:
                    break;
            }
        }
    }

    @Override
    public void clanMessageReceived(String arg0, String arg1) {

    }

    @Override
    public void personalMessageReceived(String arg0, String arg1) {

    }

    @Override
    public void playerMessageReceived(String arg0, String arg1) {
        if (!Statics.shoutCallerName.isEmpty() && Statics.shoutCallerName.equalsIgnoreCase(arg0)) {
            Statics.startPumping = !arg1.equalsIgnoreCase("stop nigga");
        }
    }

    @Override
    public void serverMessageReceived(String arg0) {

    }

    @Override
    public void tradeRequestReceived(String arg0) {

    }

    @Override
    public void paintMouse(Graphics g, Point p, Point point1) {
        ((Graphics2D) g).setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        Graphics2D spinG2 = (Graphics2D) g.create();
        spinG2.setColor(Color.ORANGE);
        spinG2.rotate(System.currentTimeMillis() % 2000d / 2000d * 360d * Math.PI / 180.0, p.x, p.y);
        spinG2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        spinG2.drawLine(p.x - 6, p.y, p.x + 6, p.y);
        spinG2.drawLine(p.x, p.y - 6, p.x, p.y + 6);
        spinG2.drawOval(p.x - 10, p.y - 10, 20, 20);
        Paint.drawTrail(g, Color.ORANGE);
    }

    @Override
    public void paintMouseSpline(Graphics g, ArrayList<Point> al) {
    }

    @Override
    public OVERRIDE_RETURN overrideKeyEvent(KeyEvent ke) {
        return OVERRIDE_RETURN.PROCESS;
    }

    @Override
    public OVERRIDE_RETURN overrideMouseEvent(MouseEvent me) {
        if (me.getID() == MouseEvent.MOUSE_CLICKED && PAINT_RECT.contains(General.getRealMousePos())) {
            showPaint = !showPaint;
        }
        return OVERRIDE_RETURN.PROCESS;
    }
}
