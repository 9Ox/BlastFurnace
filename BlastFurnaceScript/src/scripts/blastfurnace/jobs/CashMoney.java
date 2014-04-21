package scripts.blastfurnace.jobs;

import java.util.Arrays;
import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Bar;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.InventoryListener;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Statics;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox
 */
public class CashMoney extends Job implements InventoryListener {

    private final String barName;
    private final int STARTING_SECONDARY_AMOUNT = 27;
    private final int MAX_PRIMARY_AMOUNT = 27;
    private final int MAX_SECONDARY_AMOUNT = 200;
    private final RSTile[] PATH_TO_FURNACE = {new RSTile(1946, 4959, 0), new RSTile(1941, 4960, 0), new RSTile(1938, 4962, 0), new RSTile(1937, 4966, 0), 
        new RSTile(1939, 4967, 0)};
    private final RSTile[] PATH_TO_BANK = org.tribot.api2007.Walking.invertPath(PATH_TO_FURNACE);
    
    private final RSTile BAR_DISPENSER_TILE = new RSTile(1940, 4963, 0);
    private final RSTile BANK_CHEST_TILE = new RSTile(1948, 4956, 0);
    private final RSTile CONVEYOR_BELT_TILE = new RSTile(1943, 4967, 0);
    private final Cooler cooler;
    private final Bar barType;

    public CashMoney(Bar barType) {
        cooler = new Cooler();
        this.barType = barType;
        String string = barType.name().toLowerCase().concat(" bar");
        barName = Character.toString(string.charAt(0)).toUpperCase() + string.substring(1);
    }

    @Override
    public boolean shouldDo() {

        return needsToToggleRun() || getPrimaryOreAmount() != MAX_PRIMARY_AMOUNT || getSecondaryOreAmount() < MAX_SECONDARY_AMOUNT || barType.getAmountStored() > 0 || cooler.shouldDo()
                || Inventory.getCount(barName) > 0 || Player.getPosition().distanceTo(BAR_DISPENSER_TILE) > 1;
    }

    @Override
    public void doJob() {
        if (needsToToggleRun()) {
            Options.setRunOn(true);
        }

        if (getPrimaryOreAmount() == MAX_PRIMARY_AMOUNT && !cooler.shouldDo()) { // after depositong primary ores, waits for it to be cooled
            if (Player.getPosition().distanceTo(BAR_DISPENSER_TILE) > 1) {
                Walking.blindWalkTo(BAR_DISPENSER_TILE);
            }
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    // TODO Auto-generated method stub
                    return cooler.shouldDo();
                }
            }, 20000);
        }

        if (Inventory.getCount(barName) > 0 && !cooler.shouldDo()) {
            bankBars();
        } else if (isBarsCooled() && barType.getAmountStored() > 0) {
            collectBars();
        } else if (cooler.shouldDo()) {
            cooler.doJob(); // dat laze

        } else if (barType.requiresSeconary() && getSecondaryOreAmount() < STARTING_SECONDARY_AMOUNT) {
            if (Inventory.getCount(barType.getOres()[1]) == 0) {
                getOres(barType.getOres()[1]);
            } else {
                putOresIn(barType.getOres()[1]);
            }

        } else if (getPrimaryOreAmount() != MAX_PRIMARY_AMOUNT) {
            if (Inventory.getCount(barType.getOres()[0]) == 0) {
                getOres(barType.getOres()[0]);
            } else {
                putOresIn(barType.getOres()[0]);
            }

        /*} else if (barType.requiresSeconary() && getSecondaryOreAmount() < MAX_SECONDARY_AMOUNT) {
            if (Inventory.getCount(barType.getOres()[1]) == 0) {
                getOres(barType.getOres()[1]);
            } else {
                putOresIn(barType.getOres()[1]);
            }*/

        } else if (Player.getPosition().distanceTo(BAR_DISPENSER_TILE) > 1) {
            Walking.blindWalkTo(BAR_DISPENSER_TILE);
        }
    }

    /**
     * Checks whether you need to toggle run
     *
     * @return Returns true if you need to toggle run, else false
     */
    private boolean needsToToggleRun() {
        String uptext = Game.getUptext();
        return Game.getRunEnergy() > 10 && !Game.isRunOn() && uptext != null && !uptext.contains("->") && barType.getInterface() == null;
    }

    /**
     * Checks whether the bars is cooled
     *
     * @return Returns true if bars are cooled, else false
     */
    private boolean isBarsCooled() {
        int x = (Game.getSetting(543) >> 8) & 0x3;
        return x == 3;
    }

    /**
     * Gets the amount of your primary ore in the furnace
     *
     * @return the main ore amount
     */
    private int getPrimaryOreAmount() {
        return (Game.getSetting(547) >> 16);
    }

    /**
     * Gets the amount of your secondary ore in the furnace
     *
     * @return the secondary ore amount
     */
    private int getSecondaryOreAmount() {
        return (Game.getSetting(547)) & 0xFE;
    }

    /**
     * Collects bars from the dispense
     */
    private void collectBars() {
        RSObject dispenser = Get.getObject(BAR_DISPENSER_TILE);
        if (Inventory.isFull()) {
            bankBars();
        } else if (barType.getInterface() == null) {
            if (dispenser != null) {
                if (dispenser.isOnScreen()) {
                    if (Game.getUptext().contains("->")) {
                        Clicking.click(Player.getRSPlayer());
                    } else if (RSUtil.clickRSObject("Take", dispenser)) {
                        Timing.waitCondition(new Condition() {
                            @Override
                            public boolean active() {
                                return barType.getInterface() != null;
                            }
                        }, 3000);
                    }
                } else {
                    if (dispenser.getPosition().distanceTo(Player.getPosition()) <= 4) {
                        Camera.turnToTile(dispenser);
                    } else {
                        Walking.blindWalkTo(dispenser.getPosition());
                    }
                }
            }
        } else {
            if (barType.clickInterface("Take")) {
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        return Inventory.getCount(barName) > 0;
                    }
                }, 2000);
            }
        }
    }

    /**
     * Deposits bars.
     */
    private void bankBars() {
        if (!Banking.isBankScreenOpen()) {
            openBankChest();
        } else {
            if (Banking.depositAllExcept(Cooler.EMPTY_BUCKET_ID, Cooler.FULL_BUCKET_ID) > 0) {
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        return Inventory.getCount(barName) <= 0;
                    }
                }, 2000);
            }
        }
    }

    /**
     * Puts ores in the furnace. Withdraws ores if none are in inventory.
     */
    private void putOresIn(final int id) {
        String[] npcOptions = NPCChat.getOptions();
        if (npcOptions != null && npcOptions.length > 0 && Arrays.asList(npcOptions).contains("Yes")) {
            NPCChat.selectOption("Yes", true);
        } else {
            RSObject belt = Get.getObject(CONVEYOR_BELT_TILE);
            if (belt != null) {
                if (belt.isOnScreen()) {
                    if (Banking.isBankScreenOpen()) {
                        Banking.close();
                    } else if (Game.getUptext().contains("->")) {
                        Clicking.click(Player.getRSPlayer());
                    } else if (RSUtil.clickRSObject("Put-ore-on", belt)) {
                        Timing.waitCondition(new Condition() {

                            @Override
                            public boolean active() {
                                String[] npcOptions = NPCChat.getOptions();
                                return npcOptions != null && npcOptions.length > 0 && Arrays.asList(npcOptions).contains("Yes");
                            }
                        }, 9000);
                    }
                } else {
                    if (belt.getPosition().distanceTo(Player.getPosition()) <= 4) {
                        Camera.turnToTile(belt);
                    } else {
                        Walking.walkPath(false, PATH_TO_FURNACE);
                        Camera.setCameraAngle(100);
                    }
                }
            }
        }

    }

    /**
     * Withdraws the specified ore id from the bank
     *
     * @param id The ore id to withdraw
     */
    private void getOres(final int id) {
        if (Inventory.getCount(id) <= 0) {
            if (!Banking.isBankScreenOpen()) {
                openBankChest();
            } else {
                Banking.depositAllExcept(Cooler.FULL_BUCKET_ID, Cooler.EMPTY_BUCKET_ID);
                RSItem[] ore = Banking.find(id);
                if (ore.length > 0) {
                    if (ore[0] != null) {
                        if (Banking.withdrawItem(ore[0], MAX_PRIMARY_AMOUNT - getPrimaryOreAmount())) {
                            Timing.waitCondition(new Condition() {
                                @Override
                                public boolean active() {
                                    return Inventory.getCount(id) > 0;
                                }
                            }, 3000);
                        }
                    }
                }
            }
        }
    }

    /**
     * Opens bank chest
     */
    private void openBankChest() {
        RSObject chest = Get.getObject(BANK_CHEST_TILE);
        if (chest != null) {
            if (chest.isOnScreen()) {
                if (barType.getInterface() != null) {
                    Interfaces.closeAll();
                } else if (Game.getUptext().contains("->")) {
                    Clicking.click(Player.getRSPlayer());
                } else if (RSUtil.clickRSObject("Use Bank chest", chest)) {
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            return Banking.isBankScreenOpen();
                        }
                    }, 3000);
                }
            } else {
                if (chest.getPosition().distanceTo(Player.getPosition()) <= 4) {
                    Camera.turnToTile(chest);
                } else {
                    Walking.walkPath(false, PATH_TO_BANK);
                }
            }
        }
    }

    @Override
    public void inventoryItemAdded(int id, int count) {
        if (Statics.BLAST_FURNACE_AREA.contains(Player.getPosition())) {
            if (id == barType.getID()) {
                Statics.barCount += count;
            }
        }
    }

    @Override
    public void inventoryItemRemoved(int id, int count) {
    }
}
