package scripts.blastfurnace.paint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.tribot.api.General;
import org.tribot.api.input.Mouse;

/**
 * @author Starfox
 */
public class Paint {
    
    private static final LinkedList<MousePathPoint> mousePath = new LinkedList<>();
    
    /**
     * Gets the image from the specified url and returns it as an Image object.
     *
     * @param url The url to get the image from.
     * @return The image retrieved; null if no image was found.
     */
    public static Image getImageFromUrl(final String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            General.println("Failed to retreive image from: " + url);
            return null;
        }
    }
    
    private static class MousePathPoint extends Point {
        private final long finishTime;
        private final double lastingTime;

        public MousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }
    }

    /**
     * Draws a trail behind the mouse.
     * @param g Graphics object to draw from.
     * @param c Color of the trail.
     */
    public static void drawTrail(Graphics g, Color c) {
        //Mouse trail
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
            mousePath.remove();
        }
        Point clientCursor = Mouse.getPos();
        MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y,
                350); //Lasting time/MS
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) {
            mousePath.add(mpp);
        }
        MousePathPoint lastPoint = null;
        for (MousePathPoint a : mousePath) {
            if (lastPoint != null) {
                g.setColor(c);//Trail color
                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }
    }
}
 