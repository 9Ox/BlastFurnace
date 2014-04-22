package scripts.trainer;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.MouseSplinePainting;

import scripts.blastfurnace.util.WorldHop;

/**
 * @author Starfox
 */
@ScriptManifest(name = "Smither Trainer", authors = "", category = "No category")
public class Trainer extends Script implements MouseSplinePainting, MessageListening07 {

    //START VARIABLE DECLARATION
    Smithable smithable = Smithable.BRONZE_DAGGER;
    private final RSArea BLAST_FURNACE_AREA = new RSArea(new RSTile(1935, 4974, 0), new RSTile(1957, 4956, 0));
    private final int HAMMER_ID = 2347;
    private final String TRADER_NAME = "9Ox";
    private final RSTile LUMBRIDGE_TILE = new RSTile(3232, 3230, 0);
    private final static int[] JUNK_ITEMS = {1351, 590, 303, 315, 1925, 131, 2309, 1265, 1205, 1277, 1171, 841, 852, 1931, 882};
    private int STARTING_WORLD; // take away the 3 in the world
    //END VARIABLE DECLARATION

    @Override
    public void run() {
        STARTING_WORLD = Game.getCurrentWorld() - 300;
        Mouse.setSpeed(150);
        ThreadSettings.get().setClickingAPIUseDynamic(true);
        while (!BLAST_FURNACE_AREA.contains(Player.getPosition())) {
            if (needsToTrade()) {
                if (WorldHop.getWorld() != STARTING_WORLD) {
                    setLoginBotState(false);
                    if (WorldHop.switchWorld(STARTING_WORLD)) {
                        setLoginBotState(true);
                        Timing.waitCondition(new Condition() {
                            @Override
                            public boolean active() {
                                // TODO Auto-generated method stub
                                return Game.getGameState() == 30;
                            }
                        }, 10000);

                    }
                } else if (Trading.getFirstTradeInterface() != null) {
                    Trading.accept(1);
                } else if (Trading.getSecondTradeInterface() != null) {
                    Trading.accept(2);
                } else {
                    Inventory.drop(JUNK_ITEMS);
                }

            } else if (!isInVarrock()) {
                RSItem[] tab = Inventory.find(8007);
                if (tab.length > 0 && tab[0].click()) {
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            return isInVarrock();
                        }
                    }, 10500);
                }

            } else if (SKILLS.SMITHING.getActualLevel() < 60) {
                if (Camera.getCameraRotation() < 170 || Camera.getCameraRotation() > 185) {
                    Camera.setCameraRotation(General.random(170, 185));
                }
                if (Camera.getCameraAngle() < 80 || Camera.getCameraAngle() > 90) {
                    Camera.setCameraAngle(General.random(80, 90));
                }
                switchSmithable();
                if (Inventory.getCount(smithable.getBarId()) < smithable.getNumBars() || Inventory.getCount(HAMMER_ID) <= 0) {
                    if (!Banking.isBankScreenOpen()) {
                        if (!isObjectOnScreen(Objects.findNearest(20, "Bank booth"))) {
                            if (shouldWalk(new RSTile(3185, 3436, 0))) {
                                Walking.blindWalkTo(new RSTile(3185, 3436, 0));
                            }
                        } else {
                            RSObject booth = getObject("Bank booth");
                            if (booth != null && booth.click("Bank")) {
                                Timing.waitCondition(new Condition() {
                                    @Override
                                    public boolean active() {
                                        return Banking.isBankScreenOpen();
                                    }
                                }, 2500);
                            }
                        }
                    } else {
                        if (!inventoryContainsOnly(HAMMER_ID, smithable.getBarId())) {
                            if (Banking.depositAllExcept(HAMMER_ID, smithable.getBarId()) > 0) {
                                Timing.waitCondition(new Condition() {
                                    @Override
                                    public boolean active() {
                                        return inventoryContainsOnly(HAMMER_ID, smithable.getBarId());
                                    }
                                }, 1000);
                            }
                        } else if (Inventory.getCount(HAMMER_ID) <= 0) {
                            if (Banking.withdraw(1, HAMMER_ID)) {
                                Timing.waitCondition(new Condition() {
                                    @Override
                                    public boolean active() {
                                        return Inventory.getCount(HAMMER_ID) > 0;
                                    }
                                }, 4000);
                            }
                        } else {
                            if (Banking.withdraw(0, smithable.getBarId())) {
                                if (shouldWalk(new RSTile(3188, 3425, 0))) {
                                    Walking.blindWalkTo(new RSTile(3188, 3425, 0));
                                }
                                Timing.waitCondition(new Condition() {
                                    @Override
                                    public boolean active() {
                                        return !Banking.isBankScreenOpen();
                                    }
                                }, 750);
                            }
                        }
                    }
                } else {
                    if (!isObjectOnScreen(Objects.findNearest(20, "Anvil"))) {
                        if (shouldWalk(new RSTile(3188, 3425, 0))) {
                            Walking.blindWalkTo(new RSTile(3188, 3425, 0));
                        }
                    } else {
                        if (smithable.getComponent() == null) {
                            Inventory.open();
                            if (Clicking.click("Use", Inventory.find(smithable.getBarId()))) {
                                sleep(150);
                                if (Clicking.click("Anvil", Objects.findNearest(20, "Anvil"))) {
                                    Timing.waitCondition(new Condition() {
                                        @Override
                                        public boolean active() {
                                            return smithable.getComponent() != null;
                                        }
                                    }, 3000);
                                }
                            }
                        } else {
                            if (smithable.clickComponent()) {
                                if (Smithable.isEnterAmountUp()) {
                                    Keyboard.typeKey('9');
                                    Keyboard.typeKey('9');
                                    Keyboard.pressEnter();
                                }
                                for (int i = 0; i < 2500; i++) {
                                    if (Inventory.getCount(smithable.getBarId()) < smithable.getNumBars() || NPCChat.getClickContinueInterface() != null) {
                                        break;
                                    }
                                    if (Player.getAnimation() != -1) {
                                        i = 0;
                                    }
                                    sleep(1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isInVarrock() {
        return new RSArea(new RSTile(3176, 3448, 0), new RSTile(3221, 3414, 0)).contains(Player.getPosition());
    }

    private boolean isObjectOnScreen(RSObject... objects) {
        if (objects == null || objects.length <= 0) {
            return false;
        }
        if (objects[0] != null) {
            return objects[0].isOnScreen();
        }
        return false;
    }

    private RSObject getObject(String name) {
        final RSObject[] objs = Objects.findNearest(20, name);
        return objs != null && objs.length > 0 ? objs[0] : null;
    }

    private boolean needsToTrade() {
        return Player.getPosition().distanceTo(LUMBRIDGE_TILE) <= 50 && Inventory.find(8007).length == 0;
    }

    private String getFormattedName(String s) {
        return s.replaceAll(" ", " ");
    }

    private boolean clickPlayer(RSPlayer player, String option) {

        if (player != null) {
            if (player.isOnScreen()) {
                if (player.hover()) {
                    Mouse.click(3);
                }
                if (ChooseOption.isOpen()) {
                    if (ChooseOption.select(option)) {
                        return true;
                    }
                }
            } else {
                Walking.walkTo(player);
                Camera.turnToTile(player);
            }
        }
        return false;
    }

    private boolean shouldWalk(RSTile dest) {
        if (dest == null) {
            return false;
        }
        final RSTile gameDest = Game.getDestination();
        return gameDest == null || gameDest.distanceTo(dest) > 1;
    }

    /**
     * Checks to see whether or not the inventory contains only the specified ids.
     *
     * @param ids The ids to check for.
     * @return true if the inventory is empty, or contains only any of the ids specified.
     */
    private boolean inventoryContainsOnly(int... ids) {
        return Inventory.getAll().length <= Inventory.find(ids).length;
    }

    private void switchSmithable() {
        int level = Skills.SKILLS.SMITHING.getActualLevel();
        if (level >= 48) {
            smithable = Smithable.STEEL_PLATE;
        } else if (level >= 39) {
            smithable = Smithable.STEEL_WARHAMMER;
        } else if (level >= 35) {
            smithable = Smithable.STEEL_SCIMITAR;
        } else if (level >= 30) {
            smithable = Smithable.STEEL_DAGGER;
        } else if (level >= 18) {
            smithable = Smithable.BRONZE_PLATE;
        } else if (level >= 9) {
            smithable = Smithable.BRONZE_WARHAMMER;
        } else if (level >= 5) {
            smithable = Smithable.BRONZE_SCIMITAR;
        }
    }

    @Override
    public void clanMessageReceived(String arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void personalMessageReceived(String arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void playerMessageReceived(String arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void serverMessageReceived(String arg0) {
        if (arg0.contains("Accepted")) {
            setLoginBotState(false);
            if (WorldHop.switchWorld(WorldHop.getRandomWorld())) {
                setLoginBotState(true);
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        // TODO Auto-generated method stub
                        return Game.getGameState() == 30;
                    }
                }, 10000);
            }
        }
        // TODO Auto-generated method stub

    }

    @Override
    public void tradeRequestReceived(String arg0) {
        if (Inventory.find(JUNK_ITEMS).length > 0) {
            return;
        }
        if (getFormattedName(arg0).equalsIgnoreCase(TRADER_NAME)) {
            RSPlayer[] players = Players.findNearest(arg0);
            if (players.length > 0) {
                for (int i = 0; i < 10; i++) {
                    if (clickPlayer(players[0], "Trade with " + players[0].getName())) {
                        Timing.waitCondition(new Condition() {
                            @Override
                            public boolean active() {
                                // TODO Auto-generated method stub
                                return Trading.getFirstTradeInterface() != null;
                            }
                        }, 4000);
                        break;
                    }
                }
            }
        }
        // TODO Auto-generated method stub

    }

    @Override
    public void paintMouseSpline(Graphics g, ArrayList<Point> al) {
    }
}
