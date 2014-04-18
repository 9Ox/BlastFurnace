package scripts.blastfurnace.jobs;

import org.tribot.api2007.Objects;
import scripts.blastfurnace.framework.Job;

/**
 * @author Starfox, erickho123
 */
public class PipeRepairer extends Job {

    private final int[] BROKEN_PIPE_IDS = {9121};
    
    @Override
    public boolean shouldDo() {
        return Objects.findNearest(40, BROKEN_PIPE_IDS).length > 0;
    }

    @Override
    public void doJob() {
	
    }
}
