package scripts.framework;

/**
 * @author Starfox
 */
public abstract class Job {
    
    /**
     * Should be used to check whether or not the Job should be done.
     * @return true if the Job should be done; false otherwise.
     */
    public abstract boolean shouldDo();
    
    /**
     * Should be used to do the Job. Will be ran if shouldDo returns true.
     */
    public abstract void doJob();
}
