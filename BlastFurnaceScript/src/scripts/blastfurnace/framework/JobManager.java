package scripts.blastfurnace.framework;

import java.util.ArrayList;

import org.tribot.api.types.generic.Condition;

 
/**
 * @author Starfox
 */
public class JobManager {
    
    private static final ArrayList<Job> jobs = new ArrayList<>();
    private static final ArrayList<Condition> terminateConditions = new ArrayList<>();
    
    /**
     * Gets the list of jobs.
     * @return the list of jobs.
     */
    public static ArrayList<Job> getJobs() {
        return jobs;
    }
    
    /**
     * Adds a Job to the JobManager.
     * @param job The Job to add.
     */
    public static void addJob(Job job) {
        jobs.add(job);
    }
     
    /**
     * Adds a terminate condition to the JobManager.
     * @param c The condition to add.
     */
    public static void addTerminateCondition(Condition c) {
        terminateConditions.add(c);
    }
     
    /**
     * Goes through all Jobs added to the JobManager and runs the first Job that should be done, then returns if a Job was done.
     */
    public static void runJobs() {
        for (Job job : jobs) {
            if (job.shouldDo()) {
                job.doJob(); return;
            }
        }
    }
    
    /**
     * Checks whether or not the script should terminate. If any condition is met, the script will terminate.
     * @return true if any of the terminate conditions are met; false otherwise.
     */
    public static boolean shouldTerminate() {
        for (Condition c : terminateConditions) {
            if (c.active()) {
                return true;
            }
        }
        return false;
    }
}
