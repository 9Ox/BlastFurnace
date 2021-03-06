package scripts.blastfurnace.util;

import java.awt.Point;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.DPathNavigator;

/**
 * @author erickho123
 */
public class Walking {

	/**
	 * Sets the timeout to the length specified (ms)
	 *
	 * @param timeout length of time(ms) before walking times out
	 */
	public static void setWalkingTimeout(int timeout) {
		org.tribot.api2007.Walking.setWalkingTimeout(timeout);
	}

	/**
	 * Sets control click to the boolean specified
	 *
	 * @param b true to control click when walking
	 */
	public static void setControlClick(boolean b) {
		org.tribot.api2007.Walking.setControlClick(b);
	}

	/**
	 * Gets the next tile with the specified RSTile[] array
	 *
	 * @param screenWalk true to use screen to walk the path
	 * @param path the RSTile[] array of the path that it will use
	 * @return the next RSTile it should walk with the array.
	 */
	public static RSTile getNextTile(boolean screenWalk, RSTile... path) {
		if (path != null && path.length > 0) {
			for (int i = path.length - 1; i >= 0; i--) {
				if (canWalkTile(screenWalk, path[i])) {
					return path[i];
				}
			}
		}
		return null;
	}

	/**
	 * Checks if you can walk the specified RSTile[] array or not.
	 *
	 * @param screenWalk true to use screen to walk the path
	 * @param path the RSTile[] array of the path that it will use
	 * @return true if it can walk the path
	 */
	public static boolean canWalkPath(boolean screenWalk, RSTile... path) {
		if (path != null && path.length > 0) {
			for (int i = path.length - 1; i >= 0; i--) {
				Point p = screenWalk ? Projection.tileToScreen(path[i], 0) : Projection.tileToMinimap(path[i]);
				if (p != null) {
					boolean canWalk = screenWalk ? Projection.isInViewport(p) : Projection.isInMinimap(p);
					if (canWalk) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks whether you can the specified RSTile[] array or not
	 *
	 * @param screenWalk true to use screen to walk the path
	 * @param path the RSTile that it will use to check if it can walk the tile
	 * @return true if it can walk to the tile
	 */
	private static boolean canWalkTile(boolean screenWalk, RSTile tile) {
		if (tile != null) {
			Point p = screenWalk ? Projection.tileToScreen(tile, 0) : Projection.tileToMinimap(tile);
			if (p != null) {
				return screenWalk ? Projection.isInViewport(p) : Projection.isInMinimap(p);
			}
		}
		return false;
	}

	/**
	 * Walks to the specified tile using the screen
	 *
	 * @param tile the RSTile it will walk to
	 * @return true if walks to the tile
	 */
	public static boolean screenWalkTo(RSTile tile) {
		RSTile destination = Game.getDestination();
		if (destination != null && destination.distanceTo(tile) <= 2) {
			return true;
		}
		return org.tribot.api2007.Walking.walkScreenPath(new RSTile[] {tile});
	}

	/**
	 * Walks to the specified tile with the minimap
	 *
	 * @param tile the RSTile it will walk to
	 * @return true if walks to the tile
	 */
	public static boolean walkTo(RSTile tile) {
		RSTile destination = Game.getDestination();
		if (destination != null && destination.distanceTo(tile) <= 2) {
			return true;
		}
		return org.tribot.api2007.Walking.walkTo(tile);
	}

	/**
	 * Blind walks to the specified tile
	 *
	 * @param tile the RSTile it will walk to
	 * @return true if walks to the tile
	 */
	public static boolean blindWalkTo(RSTile tile) {
		RSTile destination = Game.getDestination();
		if (destination != null && destination.distanceTo(tile) <= 2) {
			return true;
		}
		return org.tribot.api2007.Walking.blindWalkTo(tile);
	}
 
	/**
	 * Gets the closest tile within the array
	 *
	 * @param tiles the RSTile[] array that it will use to get the closest tile
	 * @return the RSTile that is closest to you in the RSTile[] array
	 */
	private static RSTile getClosestTile(RSTile... tiles) {
		if (tiles != null && tiles.length > 0) {
			int tempDistance = Integer.MAX_VALUE;
			RSTile tempTile = null;
			for (RSTile tile : tiles) {
				int distance = Player.getPosition().distanceTo(tile);
				if (distance < tempDistance) {
					tempDistance = distance;
					tempTile = tile;
				}
			}
			return tempTile;
		}
		return null;
	}

	/**
	 * Walks the path
	 *
	 * @param screenWalk true if you want to use the screen to walk the path
	 * @return true if it walks the path sucessfully
	 */
	public static boolean walkPath(boolean screenWalk, RSTile... path) {
		if (canWalkPath(screenWalk, path)) {
			RSTile nextTile = getNextTile(screenWalk, path);
			if(!PathFinding.canReach(nextTile, true) && getObstacles(nextTile).length > 0)
				dealWithObstacle(nextTile);

			else
				return nextTile != null && screenWalk ? screenWalkTo(nextTile) : walkTo(nextTile);
		} else {

			RSTile closestTile = getClosestTile(path);
			if (closestTile != null && Player.getPosition().distanceTo(closestTile) <= 30) {
				RSTile[] recoverPath = new DPathNavigator().findPath(closestTile);
				General.println("recovering from lost path");
				walkPath(screenWalk, recoverPath);
				return false;
			}
		}
		return false;
	}


	/**
	 * Gets the obstacles near the specified tile
	 * 
	 * @param start, sorts the array by distance starting from specificed tile
	 * @return an array of objects surrounding the specified tile
	 */

	private static RSObject[] getObstacles(RSTile start) {
		return Objects.sortByDistance(start, Objects.findNearest(20, "Door", "Gate",
				"Gate of War", "Rickety door", "Oozing barrier",
				"Portal of Death", "Stairs", "Large door", "Prison door", "Wilderness ditch"));
	}


	/**
	 * Handles the obstacle that is in the way of the tile
	 * 
	 * @param tile the specified tile that is not reachable
	 * Deals with the obstacle near the unreachable tile
	 */
	private static void dealWithObstacle(RSTile tile) {
		RSObject[] obstacles = getObstacles(tile);
		RSInterface wildernessWarning = Interfaces.get(382, 24);
		if(wildernessWarning != null) {
			if(wildernessWarning.click("Ok"))
				Timing.waitCondition(new Condition() {

					@Override
					public boolean active() {
						// TODO Auto-generated method stub
						return Interfaces.get(382, 24) == null;
					}
				}, 4000);
		} else if(obstacles.length > 0) {
			for(RSObject o : obstacles) {
				if(PathFinding.canReach(o, true)) {
					if(o.isOnScreen()) {
						if(Clicking.click(new String[]{"Open", "Cross"}, o)) {
							General.sleep(800);
						}
					} else {
						walkTo(o.getPosition());
						Camera.turnToTile(o);
					}
					break;
				}
			}
		}
	}
}
