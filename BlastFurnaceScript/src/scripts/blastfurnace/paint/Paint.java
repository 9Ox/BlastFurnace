package scripts.blastfurnace.paint;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.tribot.api.General;

/**
 * @author Starfox
 */
public class Paint {
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
}
 