package scripts.blastfurnace.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.Screen;
import org.tribot.api2007.types.RSInterfaceChild;

public class WorldHop {

    public final static Color WORLD_SWITCH_COLOUR = new Color(189, 152, 57);
    public final Color WORLD_GREEN_ARROW_COLOUR = new Color(47, 130, 43);
    public final static Color WORLD_RED_ARROW_COLOUR = new Color(172, 12, 4);

    public final static int WORLD_PIXEL_SIZE_X = 80;
    public final static int WORLD_PIXEL_SIZE_Y = 18;

    public final static int[] WORLDS_NOT_SUPPORTED = new int[]{1, 7, 8, 37, 65, 15, 16, 23, 24, 25, 31, 37, 32, 39, 40, 47, 48, 55, 56, 63, 64, 71, 72};
    public final static int WORLD_MAXIMUM_END = 78;

    private static boolean isAtWorldHopScreen() {
        return Screen.getColorAt(11, 0).equals(WORLD_SWITCH_COLOUR);
    }

    public static boolean hasMisconfiguredWorldSettings() {
        return Screen.getColorAt(301, 9).equals(WORLD_RED_ARROW_COLOUR);
 
    }

    public static int getWorld() {
        int world = Game.getCurrentWorld();
        if (Integer.toString(world).startsWith("30")) {
            return Integer.parseInt(Integer.toString(world).split("30", 2)[1]);
        }
        return Integer.parseInt(Integer.toString(world).split("3", 2)[1]);
    }

    public static boolean switchWorld(int world) {

        long timeout = System.currentTimeMillis() + 60000;

        while (true && timeout > System.currentTimeMillis()) {
            if (Combat.isUnderAttack()) {
                General.println("under attack exiting world hop loop");
                return false;
            }
            if (Login.getLoginState() == STATE.INGAME) {
                if (GameTab.getOpen() == TABS.LOGOUT) {

                    RSInterfaceChild child = Interfaces.get(182, 6);
                    if (child != null) {
                        child.click("Ok");
                    }
                } else {
                    GameTab.open(TABS.LOGOUT);
                }
            } else if (Login.getLoginState().equals(STATE.WELCOMESCREEN)) {
                Login.login();
            } else if (isAtWorldHopScreen()) {
                if (hasMisconfiguredWorldSettings()) {
                    Mouse.click(301, 9, 1);
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            return !hasMisconfiguredWorldSettings();
                        }
                    }, 2000);
                } else {
                    Rectangle clickArea = getWorldClickArea(world);
                    Point location = new Point((int) clickArea.getCenterX(), (int) clickArea.getCenterY());
                    Mouse.hop(location);
                    General.sleep(200);
                    Mouse.sendPress(location, 1);
                    Mouse.sendRelease(location, 1);
                    Mouse.sendClickEvent(location, 1);
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            return !isAtWorldHopScreen();
                        }
                    }, 2000);

                    if (!isAtWorldHopScreen() && getWorld() == world) {
                        General.println("it returned true, we not at world hop screen and currenet world = designated world");
                        return true;
                    }
                }
            } else {

                if (getWorld() != world) {
                    Rectangle rec = new Rectangle(10, 486, 100, 493);
                    Point location = rec.getLocation();
                    Mouse.hop(location);
                    Mouse.sendPress(location, 1);
                    Mouse.sendRelease(location, 1);
                    Mouse.sendClickEvent(location, 1);

                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            return isAtWorldHopScreen();
                        }
                    }, 5000);
                }

                if (!isAtWorldHopScreen() && getWorld() == world) {
                    General.println("it returned true, currenet world = designated world");
                    return true;
                }
            }
        }
        return false;
    }


    /*private static void downloadWorldsImages() {
     for(int i = 1; i < 79; i++) {
     File file = new File(Util.getWorkingDirectory() + "/World/" + i +".png");
     if(isWorldSupported(i) && !file.isFile()) {
     General.println("WE ARE DOWNLOADING (LESS 1 MB) TOGETHER IMAGES FOR WORLDHOPPING, ONLY DOWNLOADED ONCE.");
     if(i < 10)
     General.println("DOWNLOADING WORLD: 30" + i);
     else General.println("DOWNLOADING WORLD: 3" + i);
     try {
     BufferedImage world = ImageIO.read(new URL("http://download.mutesscripts.com/ericksucks/" + i + ".png"));
     ImageIO.write(world, "png", new File(Util.getWorkingDirectory() + "/World/" + i +".png"));
     } catch (IOException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     }

     }
     }
     }
     private static boolean doesImageMatch(Image uno, Image dos) {
     final BufferedImage IMAGE_ONE = (BufferedImage) uno;
     final BufferedImage IMAGE_TWO = (BufferedImage) dos;
     for (int x = 0; x < IMAGE_ONE.getWidth(); x++) {
     for (int y = 0; y < IMAGE_ONE.getHeight(); y++) {
     if (IMAGE_ONE.getRGB(x, y) != IMAGE_TWO.getRGB(x, y))
     return false;
     }
     }
     return true;
     }


     private static boolean isWorldsImageCreated() {
     File folder = new File(Util.getWorkingDirectory() + "/World");
     if(!folder.exists())
     folder.mkdirs();

     for(int i = 1; i < 79; i++) {

     if(isWorldSupported(i)) {
     File world = new File(Util.getWorkingDirectory() + "/World/" + i +".png");
     if(!world.isFile()) {
     return false;
     }
     }
     }
     return true;
     }

     public static int getWorld() {
     Image image = Screen.getGameImage().getSubimage(65, 468, 20, 9);

     for(int i = 1; i < 79; i++) {
     if(isWorldSupported(i) && doesImageMatch(getWorldImage(i), image)) {
     return i;
     }
     }
     return -1;
     }
     public static Image getWorldImage(int world) {
     try {
     return  ImageIO.read(new File(Util.getWorkingDirectory() + "/World/" + world +".png"));
     } catch (IOException e) {
     e.printStackTrace();
     }
     return null;

     }
     */
    public static boolean isWorldSupported(int world) {
        for (int i = 0; i < WORLDS_NOT_SUPPORTED.length; i++) {
            if (WORLDS_NOT_SUPPORTED[i] == world) {
                return false;
            }
        }
        return true;
    }

    public static int getRandomWorld() {
        int randomWorld;
        while (!isWorldSupported((randomWorld = General.random(3, WORLD_MAXIMUM_END))));
        return randomWorld;
    }

    public static Rectangle getWorldClickArea(int world) {
        switch (world) {
            case 1:
                return new Rectangle(206, 73, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 2:
                return new Rectangle(206, 97, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 3:
                return new Rectangle(206, 121, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 4:
                return new Rectangle(206, 145, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 5:
                return new Rectangle(206, 169, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 6:
                return new Rectangle(206, 193, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 8:
                return new Rectangle(392, 385, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 9:
                return new Rectangle(206, 241, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 10:
                return new Rectangle(206, 265, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 11:
                return new Rectangle(206, 289, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 12:
                return new Rectangle(206, 313, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 13:
                return new Rectangle(206, 337, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 14:
                return new Rectangle(206, 361, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 17:
                return new Rectangle(206, 409, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 18:
                return new Rectangle(206, 433, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            //
            case 19:
                return new Rectangle(299, 73, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 20:
                return new Rectangle(299, 97, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 21:
                return new Rectangle(299, 121, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 22:
                return new Rectangle(299, 145, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 25:
                return new Rectangle(299, 169, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 26:
                return new Rectangle(299, 193, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 27:
                return new Rectangle(299, 217, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 28:
                return new Rectangle(299, 241, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 29:
                return new Rectangle(299, 265, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 30:
                return new Rectangle(299, 289, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 33:
                return new Rectangle(299, 313, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 34:
                return new Rectangle(299, 337, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 35:
                return new Rectangle(299, 361, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 36:
                return new Rectangle(299, 385, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 37:
                return new Rectangle(299, 409, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 38:
                return new Rectangle(299, 433, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            //
            case 41:
                return new Rectangle(392, 73, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 42:
                return new Rectangle(392, 97, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 43:
                return new Rectangle(392, 121, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 44:
                return new Rectangle(392, 145, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 45:
                return new Rectangle(392, 169, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 46:
                return new Rectangle(392, 193, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 49:
                return new Rectangle(392, 217, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 50:
                return new Rectangle(392, 241, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 51:
                return new Rectangle(392, 265, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 52:
                return new Rectangle(392, 289, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 53:
                return new Rectangle(392, 313, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 54:
                return new Rectangle(392, 337, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 57:
                return new Rectangle(392, 361, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 58:
                return new Rectangle(392, 385, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 59:
                return new Rectangle(392, 409, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 60:
                return new Rectangle(392, 433, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            //
            case 61:
                return new Rectangle(485, 73, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 62:
                return new Rectangle(485, 97, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 65:
                return new Rectangle(485, 121, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 66:
                return new Rectangle(485, 145, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 67:
                return new Rectangle(485, 169, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 68:
                return new Rectangle(485, 193, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 69:
                return new Rectangle(485, 217, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 70:
                return new Rectangle(485, 241, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 73:
                return new Rectangle(485, 265, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 74:
                return new Rectangle(485, 289, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 75:
                return new Rectangle(485, 313, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 76:
                return new Rectangle(485, 337, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 77:
                return new Rectangle(485, 361, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            case 78:
                return new Rectangle(485, 385, WORLD_PIXEL_SIZE_X, WORLD_PIXEL_SIZE_Y);
            //

        }
        return null;
    }
}
