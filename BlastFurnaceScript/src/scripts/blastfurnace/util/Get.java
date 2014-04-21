package scripts.blastfurnace.util;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Players;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;

/**
 * @author Starfox
 */
public class Get {
	/**
	 * Returns the nearest RSObject with the specified id(s) and within the specified distance.
	 * @param dist Distance to stay within.
	 * @param ids The id(s) to check for.
	 * @return The nearest RSObject found; null if none were found.
	 */
	public static RSObject getObject(final int dist, final int... ids) {
		RSObject[] objs = Objects.findNearest(dist, ids);
		return objs.length > 0 ? objs[0] : null;
	}
	
	/**
	 * Returns the nearest RSObject with the specified RSTile
	 * @param tile the RSTile the object is at
	 * @return The nearest RSObject found; null if none were found.
	 */
	public static RSObject getObject(RSTile tile) {
		RSObject[] objs = Objects.getAt(tile);
		return objs.length > 0 ? objs[0] : null;
	}
	
	/**
	 * Returns the nearest Player on the specified tile
	 * @param tile the RSTile the Player is at
	 * @return The nearest Player found on the tile; null if none were found.
	 */
	public static RSPlayer getPlayer(final RSTile tile) {
		RSPlayer[] players = Players.findNearest(new Filter<RSPlayer>() {
			@Override
			public boolean accept(RSPlayer arg0) {
				// TODO Auto-generated method stub
				return arg0.getPosition().equals(tile);
			}
		});
		return players.length > 0 ? players[0] : null;
	}


	/** 
	 * Returns the Bar from the specified String.
	 * @param barName The name of the bar
	 * Returns the bar from found the string; null if none is found
	 */
	public static Bar getBar(String barName) {
		for(Bar b : Bar.values()) {
			if(b.name().equalsIgnoreCase(barName)) {
				return b;
			}
		}
		return null;
	}
	/**
	 * Returns the nearest RSNPC with the specified id(s).
	 * @param ids The id(s) to check for.
	 * @return The nearest RSNPC found; null if none were found.
	 */
	public static RSNPC getNpc(final int... ids) {
		RSNPC[] npcs = NPCs.find(ids);
		return npcs.length > 0 ? npcs[0] : null;
	}
	
	/**
	 * Returns points within a polygon
	 * @param poly the polygon to look within
	 * @return all the points within a polygon; if none it returns an empty arraylist.
	 */
	public static ArrayList<Point> getPolyPoints(Polygon poly) {
		ArrayList<Point> p = new ArrayList<>();
		Rectangle rec = poly.getBounds();
		
		for (int x = (int) rec.getX(); x < rec.getWidth() + rec.getX(); x++) {
			for (int y = (int) rec.getY() ; y < rec.getHeight() + rec.getY(); y++) {
				if(poly.contains(x,y)) {
					p.add(new Point(x,y));
				}
			}
		}
		return p;
	}
}
