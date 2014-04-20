package scripts.blastfurnace.jobs;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Player;
import org.tribot.api2007.Screen;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.blastfurnace.framework.Job;
import scripts.blastfurnace.util.GeneralUtil;
import scripts.blastfurnace.util.Get;
import scripts.blastfurnace.util.RSUtil;
import scripts.blastfurnace.util.Walking;

public class ShoutCaller extends Job {

	private final int PARENT_ID = 30;
	private final int CHILD_ID = 3;
	private final RSTile GAUGE_TILE = new RSTile(1945,4961,0);
	private final Color SPINNER_COLOR = new Color(20, 16, 16);
	/**
	 * Heated area is approximately after half of green and stops between half of red.
	 */
	private final int[] xPoly = {175, 203, 245, 249, 239, 215, 194};
	private final int[] yPoly = {205, 195, 173, 154, 147, 130, 110};

	private final Polygon HEATED_AREA = new Polygon(xPoly, yPoly, xPoly.length);
	private ArrayList<Point> HEATED_AREA_POINTS;

	public ShoutCaller() {
		HEATED_AREA_POINTS = Get.getPolyPoints(HEATED_AREA);
	}

	@Override
	public boolean shouldDo() {
		return true;
	}

	@Override
	public void doJob() {
		RSInterfaceChild gauge = Interfaces.get(PARENT_ID, CHILD_ID);
		if (gauge != null) {
			if (needsToSayStop()) {
				Keyboard.typeSend("STOP NIGGA");
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return !needsToSayStop();
					}
				}, 20000);
			} else if (!needsToSayStop()) {
				Keyboard.typeSend("START NIGGA");
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return needsToSayStop();
					}
				}, 20000);
			}
		} else {
			clickGauge();
		}
	}

	/**
	 * Clicks the gauge, will walk to the object if it's not on screen
	 */
	private void clickGauge() {
		RSObject gauge = Get.getObject(GAUGE_TILE);
		if(gauge != null) {
			if(gauge.isOnScreen()) {
				if(RSUtil.clickRSObject("Read", gauge))
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return Interfaces.get(PARENT_ID, CHILD_ID) != null;
						}
					}, 2000);
			} else {
				if (gauge.getPosition().distanceTo(Player.getRSPlayer()) <= 4) {
					Camera.turnToTile(gauge);
				} else {
					Walking.walkTo(gauge.getPosition());
				}
			}
		}
	}



	/**
	 * Checks the area on the screen for the pointer
	 *
	 * @return true if the marker is in the red or green area
	 */
	private boolean needsToSayStop() {
		for(Point p : HEATED_AREA_POINTS) {
			Color colorP = Screen.getColorAt(p);
			if(colorP != null && colorP.equals(SPINNER_COLOR))
				return true;
		}
		return false;
	}


}
