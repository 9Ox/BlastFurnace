package scripts.blastfurnace.util;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Screen;
import org.tribot.api2007.types.RSItem;

public final class Observer extends Thread {

    private static HashSet<InventoryListener> listeners = new HashSet<>();

    private static final int SLEEP = 50;

    private final Color VALID = new Color(127, 74, 27);

    private static HashMap<Integer, Integer> last_inventory;

    public Observer() {
        last_inventory = getInventoryAsMap();
        listeners = new HashSet<>();
    }

    public void addListener(InventoryListener listener) {
        listeners.add(listener);
    } 

    @Override
    public void run() {
        while (true) {
            HashMap<Integer, Integer> currentInventory = getInventoryAsMap();
            //ADDITIONS
            for (Integer id : currentInventory.keySet()) {
                if (last_inventory.containsKey(id)) {
                    if (currentInventory.get(id) > last_inventory.get(id)) {
                        int additionCount = currentInventory.get(id) - last_inventory.get(id);
                        notifyAddition(id, additionCount);

                    }
                } else {
                    int additionCount = currentInventory.get(id);
                    notifyAddition(id, currentInventory.get(id));
                }
            }

            //REMOVALS
            for (Integer id : last_inventory.keySet()) {
                if (last_inventory.get(id) > Inventory.getCount(id)) {
                    int removeCount = (last_inventory.get(id) - Inventory.getCount(id));
                    notifyRemoval(id, removeCount);
                }
            }
            last_inventory = currentInventory;
            try {
                sleep(SLEEP);
            } catch (InterruptedException ex) {

            }
        }
    }

    private void notifyRemoval(int id, int removeCount) {
        for (InventoryListener l : listeners) {
            if (isInventoryValid() && isGameValid()) {
                l.inventoryItemRemoved(id, removeCount);
            }
        }
    }

    private void notifyAddition(int id, int count) {
        for (InventoryListener l : listeners) {
            if (isInventoryValid() && isGameValid()) {
                l.inventoryItemAdded(id, count);
            }
        }
    }

    private HashMap<Integer, Integer> getInventoryAsMap() {
        HashMap<Integer, Integer> returnMap = new HashMap<>();
        for (RSItem item : Inventory.getAll()) {
            if (item != null && !returnMap.containsKey(item.getID())) {
                returnMap.put(item.getID(), Inventory.getCount(item.getID()));
            }
        }
        return returnMap;
    }

    private boolean isInventoryValid() {
        return Screen.getColorAt(642, 180).equals(VALID);
    }

    private boolean isGameValid() {
        return Game.getGameState() == 30;
    }
}
