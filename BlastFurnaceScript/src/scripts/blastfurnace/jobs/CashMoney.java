package scripts.blastfurnace.jobs;

import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Bar;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;

/**
 * @author Starfox
 */
public class CashMoney extends Job {

    private final Bar barType;
    private final int MAX_PRIMARY_AMOUNT = 28;
    private final int MAX_SECONDARY_AMOUNT = 254;
    private final int BAR_DISPENSER_ID = 9092;
    private final Cooler cooler;
    private final String barName;
    
    public CashMoney(Bar barType) {
        cooler = new Cooler();
        this.barType = barType;
        String string = barType.name().toLowerCase().concat(" bar");
        barName = Character.toString(string.charAt(0)).toUpperCase() + string.substring(1);
    }

    @Override
    public boolean shouldDo() {
        return getPrimaryOreAmount() != MAX_PRIMARY_AMOUNT || getSecondaryOreAmount() != MAX_PRIMARY_AMOUNT || barType.getAmountStored() > 0 || cooler.shouldDo() ||
                Inventory.getCount(barName) > 0;
    }

    @Override
    public void doJob() {
        if (Inventory.getCount(barName) > 0) {
            bankBars();
        } else if (cooler.shouldDo()) {
            cooler.doJob(); // dat laze
        } else if (barType.getAmountStored() > 0) {
            collectBars();
        } else if (getPrimaryOreAmount() != MAX_PRIMARY_AMOUNT) {
            //put in primary
        } else if (getSecondaryOreAmount() != MAX_SECONDARY_AMOUNT) {

        }
    }

    /**
     * Gets the amount of your primary ore in the furnace
     *
     * @return the main ore amount
     */
    private int getPrimaryOreAmount() {
        return (Game.getSetting(547) >> 24);
    }

    /**
     * Gets the amount of your secondary ore in the furnace
     *
     * @return the secondary
     */
    private int getSecondaryOreAmount() {
        return (Game.getSetting(547)) & 0xFE;
    }

    /**
     * Collects bars from the dispense
     */
    private void collectBars() {
        RSObject dispenser = Get.getObject(40, BAR_DISPENSER_ID);
        if (barType.getInterface() == null) {
            if (dispenser != null) {
                if (dispenser.isOnScreen()) {
                    if (RSUtil.clickRSObject("Search", dispenser)) {
                        Timing.waitCondition(new Condition() {
                            @Override
                            public boolean active() {
                                return barType.getInterface() != null;
                            }
                        }, 3000);
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
        if (Inventory.getCount(id) <= 0) {
            if (!Banking.isBankScreenOpen()) {
                openBankChest();
            } else {
                if (Banking.withdraw(0, id)) {
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            return Inventory.getCount(id) > 0;
                        }
                    }, 3000);
                }
            }
        } else {
            //shove that shit in the furnace
        }
    }
    
    private void openBankChest() {
        RSObject[] chests = Objects.getAt(new RSTile(1948, 4956, 0));
            if (chests == null || chests.length <= 0) {
                return;
            }
            RSObject chest = chests[0];
            if (chest != null) {
                if (chest.isOnScreen()) {
                    if (RSUtil.clickRSObject("Use Bank chest", chest)) {
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
                        Walking.walkPath(PathFinding.generatePath(Player.getPosition(), chest, true));
                    }
                }
            }
    }
}
