package scripts.blastfurnace.util;

import java.awt.Point;
import java.awt.Rectangle;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;

public enum Minigame {

    BARBARIAN_ASSAULT(1, "Barbarian Assault"),
    BLAST_FURNACE(2, "Blast Furnace"),
    CASTLE_WARS(3, "Castle Wars"),
    FISHING_TRAWLER(4, "Fishing Trawler"),
    BURTHORPE_GAMES_ROOM(5, "Burthorpe Games Room"),
    NIGHTMARE_ZONE(6, "Nightmare Zone"),
    PEST_CONTROL(7, "Pest Control"),
    RAT_PITS(8, "Rat Pits"),
    SHADES_OF_MORTTON(9, "Shades of Mort'ton"),
    TROUBLE_BREWING(10, "Trouble Brewing"),
    TZHAAR_FIGHT_PIT(11, "TzHaar Fight Pit"), 
    PHOENIX_GANG(12, "Phoenix Gang members"),
    BLACKARM_GANG(13, "Black Arm Gang members"),
    GODWARS(14, "Godwars"),
    DAGGANOTH_KINGS(15, "Dagannoth Kings"),
    PLAYER_OWNED_HOUSE(16, "Player Owned Houses");

    private final int id;
    private final String name;

    Minigame(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private int getId() {
        return this.id;
    }

    private String getName() {
        return this.name;
    }

    public boolean teleport() {
        int timeout = 0;
        int index = getId();
        while (!GameTab.TABS.QUESTS.isOpen() && timeout < 5000) {
            Keyboard.pressFunctionKey(3);
            timeout++;
            General.sleep(1);
        }
        if (GameTab.TABS.QUESTS.isOpen() && Interfaces.get(76) == null) {
            RSInterfaceChild button = null;
            if (Interfaces.get(274) != null) {
                button = Interfaces.get(274, 12);
            } else if (Interfaces.get(259) != null) {
                button = Interfaces.get(259, 11);
            }
            if (button != null && button.click()) {
                Timing.waitCondition(new Condition() {
                    @Override
                    public boolean active() {
                        return Interfaces.get(76) != null;
                    }
                }, 4000);
            }
        }
        if (Interfaces.get(76) != null) {
            boolean selected = false;
            RSInterfaceChild menuText = Interfaces.get(76, 9);
            if (menuText != null) {
                String text = menuText.getText();
                if (text != null) {
                    if (text.equalsIgnoreCase(getName())) {
                        selected = true;
                    }
                    if (!text.contains("Select")) {
                        for (Minigame m : Minigame.values()) {
                            if (m.getName().equalsIgnoreCase(text)) {
                                index = m.getId();
                            }
                        }
                    }
                }
            }
            if (!selected) {
                final RSInterfaceChild menuButton = Interfaces.get(76, 8);
                final RSInterfaceChild menu = Interfaces.get(76, 15);
                if (menu != null && menu.isHidden()) {
                    if (menuButton != null && menuButton.click()) {
                        Timing.waitCondition(new Condition() {
                            @Override
                            public boolean active() {
                                return !menu.isHidden();
                            }
                        }, 4000);
                    }
                }
                if (menu != null && !menu.isHidden()) {
                    final RSInterfaceChild realMenu = Interfaces.get(76, 19);
                    if (realMenu != null) {
                        final RSInterfaceComponent[] list = realMenu.getChildren();
                        if (getId() > 13) {
                            final RSInterfaceChild scrollBox = Interfaces.get(76, 14);
                            if (scrollBox != null) {
                                final RSInterfaceComponent scrollBar = scrollBox.getChild(1);
                                if (scrollBar != null) {
                                    Rectangle r = scrollBar.getAbsoluteBounds();
                                    if (r != null) {
                                        Point center = new Point((int)r.getCenterX(), (int)r.getCenterY());
                                        Mouse.drag(center, new Point(center.x, center.y + 20), 1);
                                    }
                                }
                            }
                        }
                        if (list != null) {
                            int realIndex = getId();
                            if (getId() >= index) {
                                realIndex = getId() - 1;
                            }
                            if (list[realIndex].click())
                            Timing.waitCondition(new Condition() {
                                @Override
                                public boolean active() {
                                    final RSInterfaceChild menuText = Interfaces.get(76, 9);
                                    if (menuText != null) {
                                        String text = menuText.getText();
                                        return text != null && text.equalsIgnoreCase(getName());
                                    }
                                    return false;
                                }
                            }, 4000);
                        }
                    }
                }
            }
            menuText = Interfaces.get(76, 9);
            if (menuText != null) {
                String text = menuText.getText();
                if (text != null && text.equalsIgnoreCase(getName())) {
                    final RSInterfaceChild teleport = Interfaces.get(76, 25);
                    if (teleport != null) {
                        return teleport.click();
                    }
                }
            }
        }
        return false;
    }
}