package scripts.blastfurnace;

import java.awt.Graphics;
import java.util.HashMap;

import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Painting;

import scripts.blastfurnace.framework.JobLoop;
import scripts.blastfurnace.framework.JobManager;
import scripts.blastfurnace.jobs.CashMoney;
import scripts.blastfurnace.jobs.Fueler;
import scripts.blastfurnace.jobs.Pedaler;
import scripts.blastfurnace.jobs.PipeRepairer;

/**
 * @author erickho123, starfox
 */
public class BlastFurnace 
extends Script
implements Painting, Arguments {

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
				JobManager.addJob(new Pedaler());
				break;
			case "PipeRepairer":
				JobManager.addJob(new PipeRepairer());
				break;
			case "CashMoney":
				JobManager.addJob(new CashMoney());
				break;
			default:
				break;
			}
			
		}
	}
}
