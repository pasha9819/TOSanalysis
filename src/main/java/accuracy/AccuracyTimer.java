package accuracy;

import org.springframework.context.ApplicationContext;
import spring.ApplicationContextHolder;
import spring.repos.AccuracyRepo;

import java.util.HashMap;
import java.util.Timer;

/**
 * Timer, which start the <code>AccuracyTask</code>
 */
public class AccuracyTimer {
    private AccuracyTask task;
    private Timer timer;

    /**
     * Create <code>AccuracyTimer</code> and initialize <code>AccuracyTask</code>
     * @param KS_ID id of the stop (for data fetching)
     * @throws NullPointerException if there is no access to database
     */
    public AccuracyTimer(Integer KS_ID) throws NullPointerException{
        ApplicationContext ctx = ApplicationContextHolder.getApplicationContext();
        AccuracyRepo accuracyRepo = ctx.getBean(AccuracyRepo.class);
        if (accuracyRepo == null){
            throw new NullPointerException("Couldn't connect to database!");
        }
        timer = new Timer(true);
        task = new AccuracyTask(KS_ID, new HashMap<>(), accuracyRepo);
    }

    /**
     * <p>Schedules the specified task for repeated fixed-delay execution</p>
     * <p>Task is performed with a period of 15 seconds</p>
     */
    public void start(){
        timer.schedule(task, 0, 15 * 1000);
    }
}
