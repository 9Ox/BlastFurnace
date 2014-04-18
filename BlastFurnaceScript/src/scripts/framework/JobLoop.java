package scripts.framework;

import org.tribot.api.General;

/**
 * @author Starfox
 */
public class JobLoop {
    private static boolean ready = false;
    
    /**
     * Should be called once all the desired Jobs and terminate conditions have been added to the JobManager.
     * @param b true to set the script to be ready; false otherwise.
     */
    public static void setReady(boolean b) {
        ready = b;
    }
    
    /**
     * Automatically called in the waitUntilReady() method.
     * @return true if the script is ready to start; false otherwise.
     */
    private static boolean isReady() {
        return ready;
    }
    
    /**
     * Automatically called in the loop() method. Will wait indefinitely until the script is set to be ready.
     */
    private static void waitUntilReady() {
        while (!isReady()) {
            General.sleep(10);
        }
    }
    
    /**
     * Waits for the script to be ready, then loops through the JobManager's jobs.
     * In each iteration of the loop, this method will check to see if any terminate conditions are met; if any are met, the method will return.
     * This method is automatically called in the start() method.
     */
    private static void loop() {
        waitUntilReady();
        while (!JobManager.shouldTerminate()) {
            JobManager.runJobs();
        }
    }
    
    /**
     * Starts the JobLoop.
     */
    public static void start() {
        loop();
    }
}
