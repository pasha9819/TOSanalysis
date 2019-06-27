package analysis;

import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.Updater;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.API;
import tosamara.methods.json.ArrivalToStop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main {
    private static final int COUNT = 250;
    public static volatile int c = 0;
    private static long start;
    public static void main(String[] args) {
        Updater.update(false);

        /*start = System.currentTimeMillis();
        ThreadRequest t = new ThreadRequest(1);
        t.start();
        while (c != COUNT){
           Thread.yield();
        }
        System.out.println(System.currentTimeMillis() - start);*/

        int[] stopsId = new int[]{813, 170};
        for (int id : stopsId) {
            FinalStopThread t = new FinalStopThread(id);
            t.start();
        }
        while (c != COUNT){
            Thread.yield();
        }
    }

    private static class ThreadRequest extends Thread {
        int num;

        public ThreadRequest(int num) {
            this.num = num;
        }

        @Override
        public void run() {

            List<String> prevList = new ArrayList<>();
            while (Main.c < COUNT){
                try{

                    //System.out.println("--------------------------------------------------------------------------");
                    List<ArrivalToStop> list = API.getFirstArrivalToStop(new Stop(813));
                    List<String> temp = new ArrayList<>();
                    for (ArrivalToStop arrival : list){
                        //System.out.println(arrival.remainingLength + " + " + (arrival.spanLength - arrival.remainingLength));
                        if (arrival.remainingLength > 50 && arrival.spanLength - arrival.remainingLength > 50)
                            continue;
                        StringBuilder b = new StringBuilder(100);
                        Route route = RouteClassifier.findById(arrival.KR_ID);
                        if (route == null)
                            continue;
                        b.append(route.getTransportType().toString()).append(' ');
                        b.append(route.getNumber()).append(" маршрута(").append(arrival.stateNumber);
                        b.append(") ");
                        //b.append("осталось ").append(arrival.remainingLength).append(" метров до остановки ");
                        if (!route.getNumber().equals("13"))
                            continue;
                        Stop stop;
                        if (arrival.spanLength - arrival.remainingLength < 51){
                            stop = arrival.getPrevStop();
                        }else {
                            stop = StopClassifier.findById(arrival.nextStopId);
                        }
                        b.append("на остановке ");
                        if (stop == null)
                            continue;
                        b.append(stop.getTitle());

                        temp.add(b.toString());
                        if (prevList.contains(b.toString())) {
                            continue;
                        }

                        System.out.print(arrival.date.get(Calendar.HOUR_OF_DAY));
                        System.out.print(':');
                        System.out.print(arrival.date.get(Calendar.MINUTE));
                        System.out.print(':');
                        System.out.print(arrival.date.get(Calendar.SECOND));
                        System.out.print(' ');
                        System.out.println(b.toString());
                    }
                    Thread.sleep(20000);
                    prevList.clear();
                    prevList.addAll(temp);
                }catch (Throwable e){
                    e.printStackTrace();
                }

                Main.c++;
            }
        }
    }
}
