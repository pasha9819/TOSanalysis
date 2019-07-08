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

/**
 * Fetch transport arrival information task.
 */
public class AccuracyTask extends TimerTask {
    /**
     * Stop at which transport arrives.
     */
    private Stop checkedStop;

    /**
     * <p> Map <code>{hullNo : Accuracy Array}</code>.</p>
     * <p> <code>hullNo</code> - unique transport(vehicle) ID. </p>
     * <p> <code>Accuracy Array</code> - transport arrival information. </p>
     *
     */
    private HashMap<Integer, ArrayList<Accuracy>> map;

    /**
     * Database repository. (<code> table = "accuracy.accuracy" </code>)
     */
    private AccuracyRepo accuracyRepo;

    AccuracyTask(Integer KS_ID, HashMap<Integer, ArrayList<Accuracy>> map, AccuracyRepo accuracyRepo ){
        checkedStop = StopClassifier.findById(KS_ID);
        this.map = map;
        this.accuracyRepo = accuracyRepo;
    }

    /**
     * This method fetch transport arrival information from ToSamara Server
     * and add <code>Accuracy</code> objects to database.
     */
    @Override
    public void run() {
        try{
            // Get information about arriving transport and transport near the stop
            List<ArrivalToStop> arrivalList = API.getFirstArrivalToStop(checkedStop);
            List<Transport> transports;
            transports = API.getSurroundingTransports(checkedStop.getLatitude(), checkedStop.getLongitude());

            // Unique transport ID array
            List<Integer> hullNoArrivalList = new ArrayList<>();
            // Array of transports ID that disappeared from the forecast
            List<Integer> deleteList = new ArrayList<>();

            for (ArrivalToStop arrival : arrivalList) {
                hullNoArrivalList.add(arrival.getHullNo());
            }

            for(Integer i : map.keySet()){
                // If the transport was expected, but it is not in the forecast
                if (!hullNoArrivalList.contains(i)){
                    boolean offTheRoute = true;
                    for (Transport tr : transports){
                        // If the transport has disappeared from the forecast,
                        // but is located near the checkedStop
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
                        deleteList.add(i);
                    }
                }
            }
            for (Integer hullNo : deleteList){
                map.remove(hullNo);
            }
            for (ArrivalToStop arrival : arrivalList) {
                if (!map.containsKey(arrival.getHullNo())) {
                    map.put(arrival.getHullNo(), new ArrayList<>());
                }
                ArrayList<Accuracy> array = map.get(arrival.getHullNo());

                // If transport arrived at the stop
                if (arrival.isTransportNearSomeStop() && Objects.equals(arrival.getNextStopId(), checkedStop.getKS_ID())) {
                    for (Accuracy a : array) {
                        a.setRealtime((new java.util.Date().getTime() - a.getTime().getTime()) / 1000.0);
                        accuracyRepo.save(a);
                    }
                    map.remove(arrival.getHullNo());
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
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}

