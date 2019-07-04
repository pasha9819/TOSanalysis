package accuracy;


import accuracy.entity.Accuracy;
import io.IOUtil;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.Updater;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.API;
import tosamara.methods.json.ArrivalToStop;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class StopThread extends Thread {
    private Stop checkedStop;
    private HashMap<Integer, ArrayList<Accuracy>> map;

    public StopThread(Integer KS_ID) {
        checkedStop = StopClassifier.findById(KS_ID);
        setName("accuracy " + KS_ID + " stop thread");
        setDaemon(true);
        map = new HashMap<>();
    }

    @Override
    public void run() {

        while (true){
            try{

                List<ArrivalToStop> list = API.getFirstArrivalToStop(checkedStop);
                for (ArrivalToStop arrival : list){
                    if (!map.containsKey(arrival.hullNo)){
                        map.put(arrival.hullNo, new ArrayList<>());
                    }
                    ArrayList<Accuracy> array = map.get(arrival.hullNo);

                    IOUtil.println(transportPositionToString(arrival));

                    if (arrival.remainingLength < 100 && Objects.equals(arrival.nextStopId, checkedStop.getKS_ID())){
                        //
                        if(array.isEmpty()){
                            continue;
                        }
                        String s = String.format("Route = %d, StateNumber = %s", arrival.KR_ID, arrival.stateNumber);
                        IOUtil.println(s);
                        for (Accuracy a : array){
                            // 30 - amendment (because we use arrival.remainingLength < 100)
                            a.setRealtime((new java.util.Date().getTime() - a.getTime().getTime()) / 1000.0 + 30);
                            s = String.format("%4.0f %5s %4.0f", a.getForecast(), "", a.getRealtime());
                            IOUtil.println(s);
                        }
                        map.remove(arrival.hullNo);
                        continue;
                    }
                    Accuracy accuracy = new Accuracy();
                    accuracy.setForecast(arrival.timeInSeconds);
                    accuracy.setHullNo(arrival.hullNo);
                    accuracy.setRouteId(arrival.KR_ID);
                    accuracy.setStopId(checkedStop.getKS_ID());
                    accuracy.setDate(new Date(arrival.date.getTimeInMillis()));
                    accuracy.setTime(new Time(arrival.date.getTimeInMillis()));
                    array.add(accuracy);
                }
                Thread.sleep(15000);
            }catch (Throwable e){
                System.err.println(e.toString());
            }

        }
    }

    public static void main(String[] args) {

        Updater.update(false);
        int[] stopsId = new int[]{218};
        for (int id : stopsId) {
            StopThread t = new StopThread(id);
            t.start();
        }
        while (true){
            Thread.yield();
        }
    }
    private  String transportPositionToString(ArrivalToStop arrival){
        Route route = RouteClassifier.findById(arrival.KR_ID);
        if (route == null){
            return "Маршрут не определен";
        }
        String s = String.format("%4.0f  с. до ост. %25s -> %02d:%02d:%02d %10s %3s  маршрута (%s) ",
                arrival.timeInSeconds, checkedStop.getTitle(), arrival.date.get(Calendar.HOUR_OF_DAY),
                arrival.date.get(Calendar.MINUTE), arrival.date.get(Calendar.SECOND),
                route.getTransportType().toString(), route.getNumber(), arrival.stateNumber);
        StringBuilder b = new StringBuilder(s);
        if (arrival.isTransportNearStop()){
            b.append("на остановке (").append(arrival.remainingLength).append(") ");
        }else {
            b.append("в ").append(arrival.remainingLength).append(" м. до остановки ");
        }
        b.append(arrival.stop.getTitle());
        return b.toString();
    }
}

