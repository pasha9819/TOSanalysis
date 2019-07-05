package accuracy;


import spring.entity.Accuracy;
import io.IOUtil;
import org.springframework.context.ApplicationContext;
import spring.ApplicationContextHolder;
import spring.repos.AccuracyRepo;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.API;
import tosamara.methods.json.ArrivalToStop;
import tosamara.methods.json.Transport;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class AccuracyThread extends Thread {
    public static final int[] observed = new int[]{872, 222, 218, 813, 1159, 1740, 329};
    private Stop checkedStop;
    private HashMap<Integer, ArrayList<Accuracy>> map;
    private final ApplicationContext ctx;

    private AccuracyRepo accuracyRepo;


    public AccuracyThread(Integer KS_ID ) {
        checkedStop = StopClassifier.findById(KS_ID);
        setName("accuracy " + KS_ID + " stop thread");
        setDaemon(true);
        map = new HashMap<>();
        ctx = ApplicationContextHolder.getApplicationContext();
        accuracyRepo = ctx.getBean(AccuracyRepo.class);
        System.out.println("\n\n\n\n\n" + accuracyRepo + "\n\n\n\n");
    }

    @Override
    public void run() {

        while (true){
            try{

                List<ArrivalToStop> arrivalList = API.getFirstArrivalToStop(checkedStop);
                List<Transport> transports;
                transports = API.getSurroundingTransports(checkedStop.getLatitude(), checkedStop.getLongitude());

                List<Integer> hullNoArrivalList = new ArrayList<>();
                List<Integer> deleteList = new ArrayList<>();

                for (ArrivalToStop arrival : arrivalList) {
                    hullNoArrivalList.add(arrival.hullNo);
                }

                for(Integer i : map.keySet()){
                    if (!hullNoArrivalList.contains(i)){
                        boolean offTheRoute = true;
                        for (Transport tr : transports){
                            if (Objects.equals(tr.hullNo, i)){
                                ArrivalToStop arr = new ArrivalToStop();
                                arr.remainingLength = 0d;
                                arr.nextStopId = checkedStop.getKS_ID();
                                arr.stop = checkedStop;
                                arr.date = new GregorianCalendar();
                                arr.stateNumber = tr.stateNumber;
                                arr.hullNo = tr.hullNo;
                                arr.KR_ID = tr.KR_ID;
                                arr.timeInSeconds = 0d;
                                arrivalList.add(arr);
                                offTheRoute = false;
                            }
                        }
                        if (offTheRoute){
                            deleteList.add(i);
                        }
                    }
                }
                for (Integer hullNo : deleteList){
                    map.remove(hullNo);
                }
                for (ArrivalToStop arrival : arrivalList) {
                    hullNoArrivalList.add(arrival.hullNo);
                    if (!map.containsKey(arrival.hullNo)) {
                        map.put(arrival.hullNo, new ArrayList<>());
                    }
                    ArrayList<Accuracy> array = map.get(arrival.hullNo);

                    /*String str = transportPositionToString(arrival);
                    if (str != null){
                        IOUtil.println(str);
                    }*/

                    if (arrival.isTransportNearSomeStop() && Objects.equals(arrival.nextStopId, checkedStop.getKS_ID())) {
                        /*String s = String.format("Route = %d, StateNumber = %s", arrival.KR_ID, arrival.stateNumber);
                        IOUtil.println(s);
*/                        for (Accuracy a : array) {
                            a.setRealtime((new java.util.Date().getTime() - a.getTime().getTime()) / 1000.0);
                            /*s = String.format("%4.0f %5s %4.0f", a.getForecast(), "", a.getRealtime());
                            IOUtil.println(s);*/
                            accuracyRepo.save(a);
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
                e.printStackTrace();
            }

        }
    }

    /*public static void main(String[] args) {

        Updater.update(false);
        int[] stopsId = new int[]{218};
        for (int id : stopsId) {
            AccuracyThread t = new AccuracyThread(id);
            t.start();
        }
        while (true){
            Thread.yield();
        }
    }*/
    private  String transportPositionToString(ArrivalToStop arrival){
        if (arrival.timeInSeconds > 120){
            return null;
        }
        Route route = RouteClassifier.findById(arrival.KR_ID);
        if (route == null){
            return "Маршрут не определен";
        }
        String s = String.format("%4.0f  с. до ост. %25s -> %02d:%02d:%02d %10s %3s  маршрута (%s) ",
                arrival.timeInSeconds, checkedStop.getTitle(), arrival.date.get(Calendar.HOUR_OF_DAY),
                arrival.date.get(Calendar.MINUTE), arrival.date.get(Calendar.SECOND),
                route.getTransportType().toString(), route.getNumber(), arrival.stateNumber);
        StringBuilder b = new StringBuilder(s);
        if (arrival.isTransportNearSomeStop()){
            b.append("на остановке (").append(arrival.remainingLength).append(") ");
        }else {
            b.append("в ").append(arrival.remainingLength).append(" м. до остановки ");
        }
        b.append(arrival.stop.getTitle());
        return b.toString();
    }
}

