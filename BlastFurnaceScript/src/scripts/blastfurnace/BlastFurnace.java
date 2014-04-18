package scripts.blastfurnace;

import java.awt.Graphics;
import java.util.HashMap;

import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import scripts.blastfurnace.framework.JobLoop;
import scripts.blastfurnace.framework.JobManager;
import scripts.blastfurnace.jobs.CashMoney;
import scripts.blastfurnace.jobs.Fueler;
import scripts.blastfurnace.jobs.Pedaler;
import scripts.blastfurnace.jobs.PipeRepairer;
import scripts.blastfurnace.jobs.Pumper;
import scripts.blastfurnace.jobs.ShoutCaller;

/**
 * @author erickho123, starfox
 */
public class BlastFurnace 
extends Script
implements Painting, Arguments, MessageListening07 {

	/**
	 * Creates a new BlastFurnace object.
	 * Should be used to initialize any class specific variables and set any internal operations.
	 */
	public BlastFurnace() {
		ThreadSettings.get().setClickingAPIUseDynamic(true);
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
		if(input != null) {
			switch(input) {
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
				JobManager.addJob(new CashMoney());
				break;
			case "ShoutCaller":
				JobManager.addJob(new ShoutCaller());
				break;
			default:
				break;
			}

		}
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
		if(!Pumper.shoutCallerName.isEmpty() && Pumper.shoutCallerName.equalsIgnoreCase(arg0)) {
			if(arg1.contains("STOP NIGGA")) {
				Pumper.startPumping = false;
			} else {
				Pumper.startPumping = true;
			}
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void serverMessageReceived(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub

	}
}
