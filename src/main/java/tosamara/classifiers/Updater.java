package tosamara.classifiers;


import tosamara.classifiers.grabbers.Grabber;
import tosamara.classifiers.grabbers.RouteGrabber;
import tosamara.classifiers.grabbers.StopGrabber;

public class Updater {
    private static Grabber[] grabbers = new Grabber[]{
            new StopGrabber(),
            new RouteGrabber()
    };

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
            g.updateAndLoad();
            System.out.print("Update " + g.getClass().getSimpleName() + ' ' +
                    (System.currentTimeMillis() - start) + "ms  ");
            System.out.println(Thread.currentThread().getName());
        }
    }
}
