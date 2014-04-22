package scripts.trainer;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import org.tribot.api.Clicking;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MouseSplinePainting;

/**
 * @author Starfox
 */
@ScriptManifest(name = "Smither Trainer", authors = "", category = "No category")
public class Trainer extends Script implements MouseSplinePainting {

    //START VARIABLE DECLARATION
    Smithable smithable = Smithable.BRONZE_DAGGER;
    private final RSArea BLAST_FURNACE_AREA = new RSArea(new RSTile(1935, 4974, 0), new RSTile(1957, 4956, 0));
    private final int HAMMER_ID = 2347;
    //END VARIABLE DECLARATION

    @Override
    public void run() {
        Mouse.setSpeed(128);
        while (!BLAST_FURNACE_AREA.contains(Player.getPosition())) {
            if (isInVarrock() && Skills.SKILLS.SMITHING.getActualLevel() < 60) {
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
                            Banking.depositAllExcept(HAMMER_ID, smithable.getBarId());
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
                            if (Clicking.click("Use", Inventory.find(smithable.getBarId()))) {
                                if (Clicking.click("Anvil", Objects.find(20, "Anvil"))) {
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

    private boolean shouldWalk(RSTile dest) {
        if (dest == null) {
            return false;
        }
        final RSTile gameDest = Game.getDestination();
        return gameDest == null || gameDest.distanceTo(dest) <= 1;
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
        switch (Skills.SKILLS.SMITHING.getActualLevel()) {
            case 5:
                smithable = Smithable.BRONZE_SCIMITAR;
                break;
            case 9:
                smithable = Smithable.BRONZE_WARHAMMER;
                break;
            case 18:
                smithable = Smithable.BRONZE_PLATE;
                break;
            case 30:
                smithable = Smithable.STEEL_DAGGER;
                break;
            case 35:
                smithable = Smithable.STEEL_SCIMITAR;
                break;
            case 39:
                smithable = Smithable.STEEL_WARHAMMER;
                break;
            case 48:
                smithable = Smithable.STEEL_PLATE;
                break;
        }
    }

    @Override
    public void paintMouseSpline(Graphics g, ArrayList<Point> al) {
    }
}
