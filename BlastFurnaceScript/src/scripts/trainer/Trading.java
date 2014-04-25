package scripts.trainer;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceMaster;

public class Trading {
 


	public static RSInterfaceMaster getFirstTradeInterface() {
		return Interfaces.get(335);
	}

	public static RSInterfaceMaster getSecondTradeInterface() {
		return Interfaces.get(334);
	}

	public static boolean accept(int type) {
		if(type == 1) {
			RSInterface firstScreen = getFirstTradeInterface();
			if(firstScreen != null) {
				RSInterface acceptButton = firstScreen.getChild(17);
				return acceptButton != null && acceptButton.click("Accept trade");
			}
		} else if(type == 2) {
			RSInterface secondScreen = getSecondTradeInterface();
			if(secondScreen != null) {
				RSInterface acceptButton = secondScreen.getChild(20);
				return acceptButton != null && acceptButton.click("Accept");
			}	
		}
		return false;
	}
}
