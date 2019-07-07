package accuracy;

import org.springframework.context.ApplicationContext;
import spring.ApplicationContextHolder;
import spring.entity.Accuracy;
import spring.repos.AccuracyRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class AccuracyTimer {
    private HashMap<Integer, ArrayList<Accuracy>> map;
    private AccuracyTask task;

    public AccuracyTimer(Integer KS_ID) throws NullPointerException{
        ApplicationContext ctx = ApplicationContextHolder.getApplicationContext();
        AccuracyRepo accuracyRepo = ctx.getBean(AccuracyRepo.class);
        if (accuracyRepo == null){
            throw new NullPointerException("Couldn't connect to database!");
        }
        task = new AccuracyTask(KS_ID, new HashMap<>(), accuracyRepo);
    }

    public void start(){
        Timer t = new Timer(true);
        t.schedule(task, 0, 15 * 1000);
    }
}
