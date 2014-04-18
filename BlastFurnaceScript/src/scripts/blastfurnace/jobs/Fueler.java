package scripts.blastfurnace.jobs;

import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSObject;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox, erickho123
 */ 
public class Fueler extends Job {

	private final int SPADE_ID = 952;
	private final int COKE_ID = 6448;

	@Override
	public boolean shouldDo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doJob() {
		if(Inventory.getCount(COKE_ID) > 0) {
			refuelStove();
		} else if(Inventory.getCount(SPADE_ID) > 0) {
			collectCoke();
		} else {
			pickupSpade();
		}
		// TODO Auto-generated method stub

	}


	private void pickupSpade() {
		RSGroundItem spade = null;
		RSGroundItem[] spades = GroundItems.findNearest(SPADE_ID);
		if(spades.length > 0) {
			spade = spades[0];
			if(spade != null) {
				if(spade.isOnScreen()) {
					if(RSUtil.clickRSGroundItem("Take ", spade))
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								// TODO Auto-generated method stub
								return Inventory.getCount(SPADE_ID) > 0;
							}
						}, 2000);
					
				} else {
					if (spade.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
						Camera.turnToTile(spade);
					} else {
						Walking.walkTo(spade.getPosition());
					}
				}
			}
		}
	}


	private void collectCoke() {
		RSObject coke = null;
		RSObject[]  cocacolas = Objects.findNearest(40, "Coke");
		if(cocacolas.length > 0) {
			coke = cocacolas[0];
			if(coke != null) {
				if(coke.isOnScreen()) {
					if(RSUtil.clickRSObject("Collect", coke))
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								// TODO Auto-generated method stub
								return Inventory.getCount(COKE_ID) > 0;
							}
						}, 2000);
				} else {
					if (coke.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
						Camera.turnToTile(coke);
					} else {
						Walking.walkTo(coke.getPosition());
					}
				}
			}
		}
	}

	private void refuelStove() {
		RSObject stove = null;
		RSObject[] stoves = Objects.findNearest(40, "Stove");
		if(stoves.length > 0) {
			stove = stoves[0];
			if(stove != null) {
				if(stove.isOnScreen()) {
					if(RSUtil.clickRSObject("Refuel", stove))
						Timing.waitCondition(new Condition() {

							@Override
							public boolean active() {
								// TODO Auto-generated method stub
								return Inventory.getCount(COKE_ID) == 0;
							}
						}, 2000);
				} else {
					if (stove.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
						Camera.turnToTile(stove);
					} else {
						Walking.walkTo(stove.getPosition());
					}
				}
			}
		}
	}
}


