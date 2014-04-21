package scripts.blastfurnace.jobs;

import org.tribot.api.Clicking;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

/**
 * @author Starfox
 */
public class Cooler extends Job {

	public static final int EMPTY_BUCKET_ID = 1925;
	public static final int FULL_BUCKET_ID = 1929;
	private final RSTile SINK_TILE = new RSTile(1942,4956,0);
	private final RSTile BAR_DISPENSER_TILE = new RSTile(1940, 4963, 0);
	private final int DISPENSER_SETTING_INDEX = -1;
	@Override
	public boolean shouldDo() {
		return needsToCoolBar() || ((Inventory.getCount(EMPTY_BUCKET_ID) <= 0 && Inventory.getCount(FULL_BUCKET_ID) <= 0)
				|| Inventory.getCount(EMPTY_BUCKET_ID) > 0);
	}

	@Override
	public void doJob() {
		if (Inventory.getCount(EMPTY_BUCKET_ID) <= 0 && Inventory.getCount(FULL_BUCKET_ID) <= 0) {
			getBucket();
		} else if (Inventory.getCount(EMPTY_BUCKET_ID) > 0) {
			fillBucket();
		} else {
			coolBars();
		}
	}


	private boolean needsToCoolBar() {
		return Game.getSetting(DISPENSER_SETTING_INDEX) == 1;
	}

	/**
	 * Picks up the bucket in the blast furnace.
	 */
	private void getBucket() {
		RSGroundItem[] buckets = GroundItems.find(EMPTY_BUCKET_ID);
		RSGroundItem bucket;
		if (buckets.length > 0) {
			bucket = buckets[0];
			if (bucket != null) {
				if (bucket.isOnScreen()) {
					if (RSUtil.clickRSGroundItem("Take Bucket", bucket)) {
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								return Inventory.getCount(EMPTY_BUCKET_ID) > 0;
							}
						}, 4000);
					}
				} else {
					if (bucket.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
						Camera.turnToTile(bucket);
					} else {
						Walking.blindWalkTo(bucket.getPosition());
					}
				}
			}
		}
	}

	/**
	 * Fills an empty bucket in the blast furnace sink.
	 */
	private void fillBucket() {
		RSObject sink = Get.getObject(SINK_TILE);
		if (sink != null) {
			if (sink.isOnScreen()) {
				if (Clicking.click("Use", Inventory.find(EMPTY_BUCKET_ID))) {
					if (RSUtil.clickRSObject("Use", sink)) {
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								return Inventory.getCount(FULL_BUCKET_ID) > 0;
							}
						}, 4000);
					}
				}
			} else {
				if (sink.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
					Camera.turnToTile(sink);
				} else {
					Walking.blindWalkTo(sink.getPosition());
				}
			}
		}
	}

	private void coolBars() {
		RSObject dispenser = Get.getObject(BAR_DISPENSER_TILE);
		if (dispenser != null) {
			if (dispenser.isOnScreen()) {
				if (Clicking.click("Use", Inventory.find(FULL_BUCKET_ID))) {
					if (dispenser.click("Use")) {
						Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								return false; //check if bars cooled
							}
						}, 3000);
					}
				}
			} else {
				if (dispenser.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
					Camera.turnToTile(dispenser);
				} else {
					Walking.walkPath(false, PathFinding.generatePath(Player.getPosition(), dispenser, true));
				}
			}
		}
	}
}
