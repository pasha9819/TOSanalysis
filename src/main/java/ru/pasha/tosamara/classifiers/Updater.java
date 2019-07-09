package ru.pasha.tosamara.classifiers;

import ru.pasha.tosamara.classifiers.grabbers.Grabber;
import ru.pasha.tosamara.classifiers.grabbers.RouteGrabber;
import ru.pasha.tosamara.classifiers.grabbers.StopGrabber;

/**
 * Class helping to update classifiers.
 */
public class Updater {
    /**
     * List of grabbers, which download data from remote server.
     */
    private static Grabber[] grabbers = new Grabber[]{
            new StopGrabber(),
            new RouteGrabber()
    };

    /**
     * Running classifier update.
     * <p>If <code>inNewThread = true</code> update will be performed in a new thread,
     * else - in current thread</p>
     * @param inNewThread launch flag
     */
    public static void update(boolean inNewThread) {
        if (inNewThread){
            new Thread(Updater::updateAllClassifiers).start();
        }else {
            updateAllClassifiers();
        }
    }

    private static void updateAllClassifiers(){
        long start;
        for (Grabber g : grabbers) {
            start = System.currentTimeMillis();
            g.downloadAndUpdate();
            System.out.print("Update " + g.getClass().getSimpleName() + ' ' +
                    (System.currentTimeMillis() - start) + "ms  ");
            System.out.println(Thread.currentThread().getName());
        }
    }
}
