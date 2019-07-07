package accuracy;

import spring.entity.Accuracy;
import spring.repos.AccuracyRepo;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.API;
import tosamara.methods.json.ArrivalToStop;
import tosamara.methods.json.Transport;

import java.sql.Date;
import java.sql.Time;
import java.util.*;

public class AccuracyTask extends TimerTask {
    private Stop checkedStop;
    private HashMap<Integer, ArrayList<Accuracy>> map;
    private AccuracyRepo accuracyRepo;

    public AccuracyTask(Integer KS_ID,
                        HashMap<Integer, ArrayList<Accuracy>> map,
                        AccuracyRepo accuracyRepo ){
        checkedStop = StopClassifier.findById(KS_ID);
        this.map = map;
        this.accuracyRepo = accuracyRepo;
    }

    @Override
    public void run() {
        try{
            System.out.println("-------------------------------");
            System.out.println("map " + map.size());
            List<ArrivalToStop> arrivalList = API.getFirstArrivalToStop(checkedStop);
            List<Transport> transports;
            transports = API.getSurroundingTransports(checkedStop.getLatitude(), checkedStop.getLongitude());

            List<Integer> hullNoArrivalList = new ArrayList<>();
            List<Integer> deleteList = new ArrayList<>();

            for (ArrivalToStop arrival : arrivalList) {
                hullNoArrivalList.add(arrival.getHullNo());
            }

            for(Integer i : map.keySet()){
                if (!hullNoArrivalList.contains(i)){
                    System.out.print( "пропал ");
                    boolean offTheRoute = true;
                    for (Transport tr : transports){
                        if (Objects.equals(tr.getHullNo(), i)){
                            ArrivalToStop arr;
                            arr = new ArrivalToStop(new GregorianCalendar(), tr.getStateNumber(), tr.getKR_ID(),
                                    tr.getHullNo(), checkedStop.getKS_ID(), 0d,
                                    0d, 0d);
                            arrivalList.add(arr);
                            offTheRoute = false;
                        }
                    }
                    if (offTheRoute){
                        System.out.println("совсем");
                        deleteList.add(i);
                    }else {
                        System.out.println();
                    }
                }
            }
            for (Integer hullNo : deleteList){
                map.remove(hullNo);
            }
            for (ArrivalToStop arrival : arrivalList) {
                System.out.println(arrival.getRemainingLength() + " до "
                        + StopClassifier.findById(arrival.getNextStopId()).getTitle() + " госномер = "
                        + arrival.getStateNumber());
                if (!map.containsKey(arrival.getHullNo())) {
                    map.put(arrival.getHullNo(), new ArrayList<>());
                }
                ArrayList<Accuracy> array = map.get(arrival.getHullNo());

                if (arrival.isTransportNearSomeStop() && Objects.equals(arrival.getNextStopId(), checkedStop.getKS_ID())) {
                    for (Accuracy a : array) {
                        a.setRealtime((new java.util.Date().getTime() - a.getTime().getTime()) / 1000.0);
                        accuracyRepo.save(a);
                    }
                    map.remove(arrival.getHullNo());
                    System.out.println("приехал " + arrival.getStateNumber());
                    System.out.println();
                    continue;
                }
                Accuracy accuracy = new Accuracy();
                accuracy.setForecast(arrival.getTimeInSeconds());
                accuracy.setHullNo(arrival.getHullNo());
                accuracy.setRouteId(arrival.getKR_ID());
                accuracy.setStopId(checkedStop.getKS_ID());
                accuracy.setDate(new Date(arrival.getDate().getTimeInMillis()));
                accuracy.setTime(new Time(arrival.getDate().getTimeInMillis()));
                array.add(accuracy);
            }
            System.out.println();
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}

