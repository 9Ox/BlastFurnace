package scripts.blastfurnace.framework;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.blastfurnace.BlastFurnace;
import scripts.blastfurnace.util.Minigame;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Statics;
import scripts.blastfurnace.util.Walking;
import scripts.blastfurnace.util.WorldHop;

/**
 * @author Starfox
 */
public class JobLoop {

	private static boolean ready = false;

	/**
	 * Should be called once all the desired Jobs and terminate conditions have been added to the JobManager.
	 *
	 * @param b true to set the script to be ready; false otherwise.
	 */
	public static void setReady(boolean b) {
		ready = b;
	}

	/**
	 * Automatically called in the waitUntilReady() method.
	 *
	 * @return true if the script is ready to start; false otherwise.
	 */
	private static boolean isReady() {
		return ready;
	}

	/**
	 * Automatically called in the loop() method. Will wait indefinitely until the script is set to be ready. This method will also load any jobs that were added in the
	 * JobLoader class.
	 */
	private static void waitUntilReady() {
		while (!isReady()) {
			General.sleep(10);
		}
		JobLoader.loadJobs();
	}

	/**
	 * Waits for the script to be ready, then loops through the JobManager's jobs. In each iteration of the loop, this method will check to see if any terminate conditions are
	 * met; if any are met, the method will return. This method is automatically called in the start() method.
	 */
	private static void loop() {
		waitUntilReady();
		while (!JobManager.shouldTerminate()) {
			if (WorldHop.getWorld() != Statics.startWorld) {
				BlastFurnace.script.setLoginBotState(false);
				if (WorldHop.switchWorld(Statics.startWorld)) {
					BlastFurnace.script.setLoginBotState(true);
				}
			} else if(!Statics.BLAST_FURNACE_AREA.contains(Player.getPosition())) {
				boolean execute = false;
				for(int i = 0; i < 10000; i++) {
					if(Statics.BLAST_FURNACE_AREA.contains(Player.getPosition()))
						break;
					if(i == 9)
						execute = true;
					General.sleep(200,300);
				}
				if(execute) {
					RSObject[] stairs = Objects.getAt(new RSTile(2930,10196,0));
					if(stairs.length > 0) {
						if(PathFinding.canReach(stairs[0], true)) {
							if(stairs[0].isOnScreen()) {
								if(RSUtil.clickRSObject("Climb-down", stairs))
									Timing.waitCondition(new Condition() {

										@Override
										public boolean active() {
											// TODO Auto-generated method stub
											return Statics.BLAST_FURNACE_AREA.contains(Player.getPosition());
										}
									}, 4000);

							} else {
								Walking.walkTo(stairs[0].getPosition());
								Camera.turnToTile(stairs[0].getPosition());
							}
						} else {
							Walking.walkPath(false, org.tribot.api2007.Walking.generateStraightPath(stairs[0]));
						}
					} else if(Minigame.BLAST_FURNACE.teleport()) {
						if(Timing.waitCondition(new Condition() {
							@Override
							public boolean active() {
								// TODO Auto-generated method stub
								return Player.getAnimation() != -1;
							}
						}, 4000)) {
							Timing.waitCondition(new Condition() {
								@Override
								public boolean active() {
									// TODO Auto-generated method stub
									return  Objects.getAt(new RSTile(2930,10196,0)).length > 0 || Statics.BLAST_FURNACE_AREA.contains(Player.getPosition());
								}
							}, 30000);
						}
					}
				}
			} else {
				JobManager.runJobs();
				if (General.random(0, 1000) == 0) {
					boolean neg = General.random(0, 1) == 0;
					Camera.setCameraRotation(Camera.getCameraRotation() + (neg ? Camera.getCameraRotation() - General.random(10, 90) : 
						Camera.getCameraRotation() + General.random(10, 90)));
				}
			}
			General.sleep(10);
		}
	}

	/**
	 * Starts the JobLoop.
	 */
	public static void start() {
		loop();
	}
}
