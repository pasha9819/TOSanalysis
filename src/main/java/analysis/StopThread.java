package analysis;

import io.IOUtil;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.API;
import tosamara.methods.json.ArrivalToStop;

import java.util.*;

import static java.lang.Math.abs;

public class CheckStopThread extends Thread {
    private Stop chekedStop;
    private Timer timer;
    private volatile List<ArrivalToStop> prevList = new ArrayList<>();

    CheckStopThread(Integer KS_ID) {
        chekedStop = StopClassifier.findById(KS_ID);
        setName("chekedStop " + KS_ID + " thread");
        setDaemon(true);
        // timer - daemon
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new Cleaner(), 0, 5 * 60 * 1000);
    }

    @Override
    public void run() {

        while (true){
            try{

                List<ArrivalToStop> list = API.getFirstArrivalToStop(chekedStop);
                List<ArrivalToStop> temp = new ArrayList<>();
                for (ArrivalToStop arrival : list){
                    if (!arrival.isTransportNearStop()){
                        continue;
                    }
                    boolean transportIsMove = true;
                    for(ArrivalToStop arr : prevList){
                        // If transport stay near the chekedStop, we will not work with this transport.

                        if (Objects.equals(arrival.hullNo, arr.hullNo)
                            && dateDifference(arrival.date, arr.date) < 5 * 60 * 1000
                            && Objects.equals(arrival.nextStopId, arr.nextStopId))
                        {
                            transportIsMove = false;
                            // update date+time
                            arr.date = arrival.date;
                            //IOUtil.println("\t\t" + arrival.stateNumber + " все еще на остановке " + arrival.stop.getTitle());
                        }
                    }
                    if (transportIsMove){
                        temp.add(arrival);
                    }else {
                        continue;
                    }

                    IOUtil.println(transportPositionToString(arrival));
                }
                Thread.sleep(15000);
                prevList.addAll(temp);
            }catch (Throwable e){
                System.err.println(e.toString());
            }

        }
    }

    @Override
    public void interrupt() {
        timer.cancel();
        super.interrupt();
    }



    private class Cleaner extends TimerTask {
        @Override
        public void run() {
            IOUtil.println("Cleaner is work!");
            if (prevList != null){
                int deleted = prevList.size() * 4 / 5;
                if (deleted > 0) {
                    prevList.subList(0, deleted).clear();
                }
            }
        }
    }

    private String transportPositionToString(ArrivalToStop arrival){
        Route route = RouteClassifier.findById(arrival.KR_ID);
        if (route == null){
            return "Маршрут не определен";
        }
        String s = String.format("%4.0f  с. до ост. %25s -> %02d:%02d:%02d %10s %3s  маршрута (%s) ",
                arrival.timeInSeconds, chekedStop.getTitle(), arrival.date.get(Calendar.HOUR_OF_DAY),
                arrival.date.get(Calendar.MINUTE), arrival.date.get(Calendar.SECOND),
                route.getTransportType().toString(), route.getNumber(), arrival.stateNumber);
        StringBuilder b = new StringBuilder(s);
        if (abs(arrival.remainingLength) < 50){
            b.append("на остановке (").append(arrival.remainingLength).append(") ");
        }else {
            b.append("в ").append(arrival.remainingLength).append(" м. до остановки ");
        }
        b.append(arrival.stop.getTitle());
        return b.toString();
    }

    private long dateDifference(Calendar one, Calendar two){
        return abs(one.getTimeInMillis() - two.getTimeInMillis());
    }
}
