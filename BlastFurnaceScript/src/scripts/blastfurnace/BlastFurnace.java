package scripts.blastfurnace;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;

import org.tribot.api2007.Game;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.MousePainting;
import org.tribot.script.interfaces.MouseSplinePainting;
import org.tribot.script.interfaces.Painting;

import scripts.blastfurnace.framework.JobLoop;
import scripts.blastfurnace.framework.JobManager;
import scripts.blastfurnace.jobs.CashMoney;
import scripts.blastfurnace.jobs.Cooler;
import scripts.blastfurnace.jobs.Fueler;
import scripts.blastfurnace.jobs.Pedaler;
import scripts.blastfurnace.jobs.PipeRepairer;
import scripts.blastfurnace.jobs.Pumper;
import scripts.blastfurnace.jobs.ShoutCaller;
import scripts.blastfurnace.paint.Paint;
import scripts.blastfurnace.util.Get;

/**
 * @author erickho123, starfox
 */
public class BlastFurnace
extends Script
implements Painting, Arguments, MessageListening07, MousePainting, MouseSplinePainting {

	/**
	 * Creates a new BlastFurnace object. Should be used to initialize any class specific variables and set any internal operations.
	 */
	public BlastFurnace() {
		JobLoop.setReady(true);
	}

	@Override
	public void run() {
		JobLoop.start();
	}

	@Override
	public void onPaint(Graphics g1) {
	}

	@Override
	public void passArguments(HashMap<String, String> arg0) {
		String input = arg0.get("custom_input");
		if (input != null) {
			switch (input) {
			case "Fueler":
				JobManager.addJob(new Fueler());
				break;
			case "Pedaler":
				Pumper.shoutCallerName = input.split(":")[1];
				JobManager.addJob(new Pedaler());
				break;
			case "PipeRepairer":
				JobManager.addJob(new PipeRepairer());
				break;
			case "CashMoney":
				CashMoney.barType = Get.getBar(input.split(":")[1]);
				JobManager.addJob(new CashMoney());
				break;
			case "ShoutCaller":
				JobManager.addJob(new ShoutCaller());
				break;
			case "Cooler":
				JobManager.addJob(new Cooler());
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {

	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {

	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		if (!Pumper.shoutCallerName.isEmpty() && Pumper.shoutCallerName.equalsIgnoreCase(arg0)) {
			Pumper.startPumping = !arg1.equalsIgnoreCase("stop nigga");
		}
	}

	@Override
	public void serverMessageReceived(String arg0) {

	}

	@Override
	public void tradeRequestReceived(String arg0) {

	}

	@Override
	public void paintMouse(Graphics g, Point p, Point point1) {
		((Graphics2D) g).setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		Graphics2D spinG2 = (Graphics2D) g.create();
		spinG2.setColor(Color.ORANGE);
		spinG2.rotate(System.currentTimeMillis() % 2000d / 2000d * 360d * Math.PI / 180.0, p.x, p.y);
		spinG2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		spinG2.drawLine(p.x - 6, p.y, p.x + 6, p.y);
		spinG2.drawLine(p.x, p.y - 6, p.x, p.y + 6);
		spinG2.drawOval(p.x - 10, p.y - 10, 20, 20);
		Paint.drawTrail(g, Color.ORANGE);
	}

	@Override
	public void paintMouseSpline(Graphics g, ArrayList<Point> al) {
	}
}
