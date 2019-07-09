package ru.pasha.spring.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pasha.spring.entity.Accuracy;
import ru.pasha.spring.repos.AccuracyRepo;
import ru.pasha.tosamara.classifiers.StopClassifier;
import ru.pasha.tosamara.classifiers.Updater;
import ru.pasha.tosamara.classifiers.xml.stop.Stop;
import ru.pasha.tosamara.methods.API;
import ru.pasha.tosamara.methods.json.ArrivalToStop;
import ru.pasha.tosamara.methods.json.Transport;

import javax.annotation.PostConstruct;
import java.sql.Time;
import java.util.*;

@Service
@ConditionalOnProperty(name = "app.fetchdata")
public class DataFetchService {
    /**
     * Stops for which data is collected.
     * (Array consists of stops id.)
     */
    public static final int[] OBSERVED_STOPS_ID = new int[]{872, 222, 218, 813, 1159, 1740, 329};

    /**
     * Stops at which transport arrives.
     */
    private ArrayList<Stop> observedStops;

    /**
     * <p> Map <code>{hullNo : Accuracy Array}</code>.</p>
     * <p> <code>hullNo</code> - unique transport(vehicle) ID. </p>
     * <p> <code>Accuracy Array</code> - transport arrival information. </p>
     *
     */
    private ArrayList<HashMap<Integer, ArrayList<Accuracy>>> maps;

    private AccuracyRepo accuracyRepo;

    @Autowired
    DataFetchService(AccuracyRepo accuracyRepo){
        this.accuracyRepo = accuracyRepo;
        maps = new ArrayList<>();
        for (int i : OBSERVED_STOPS_ID) {
            maps.add(new HashMap<>());
        }
        observedStops = new ArrayList<>();
    }

    @PostConstruct
    private void updateClassifier(){
        //Updater.update(false);
        for(int id : OBSERVED_STOPS_ID){
            Stop s = StopClassifier.findById(id);
            if (s != null){
                observedStops.add(s);
            }
        }
    }

    @Scheduled(initialDelay = 20000, fixedRate = 15000)
    public void fetch(){
        Stop checkedStop;
        HashMap<Integer, ArrayList<Accuracy>> map;
        for(int i = 0; i < OBSERVED_STOPS_ID.length; i++){
            try{
                checkedStop = observedStops.get(i);
                map = maps.get(i);
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

                for(Integer hullNo : map.keySet()){
                    // If the transport was expected, but it is not in the forecast
                    if (!hullNoArrivalList.contains(hullNo)){
                        boolean offTheRoute = true;
                        for (Transport tr : transports){
                            // If the transport has disappeared from the forecast,
                            // but is located near the checkedStop
                            if (Objects.equals(tr.getHullNo(), hullNo)){
                                ArrivalToStop arr;
                                arr = new ArrivalToStop(new GregorianCalendar(), tr.getStateNumber(), tr.getRouteId(),
                                        tr.getHullNo(), checkedStop.getId(), 0d,
                                        0d, 0d);
                                arrivalList.add(arr);
                                offTheRoute = false;
                            }
                        }
                        if (offTheRoute){
                            deleteList.add(hullNo);
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
                    if (arrival.isTransportNearSomeStop() && Objects.equals(arrival.getNextStopId(), checkedStop.getId())) {
                        for (Accuracy a : array) {
                            a.setRealTime((new java.util.Date().getTime() - a.getTime().getTime()) / 1000.0);
                            accuracyRepo.save(a);
                        }
                        map.remove(arrival.getHullNo());
                        continue;
                    }
                    Accuracy accuracy = new Accuracy();
                    accuracy.setForecast(arrival.getTimeInSeconds());
                    accuracy.setHullNo(arrival.getHullNo());
                    accuracy.setRouteId(arrival.getRouteId());
                    accuracy.setStopId(checkedStop.getId());
                    accuracy.setDate(new java.sql.Date(arrival.getDate().getTimeInMillis()));
                    accuracy.setTime(new Time(arrival.getDate().getTimeInMillis()));
                    array.add(accuracy);
                }
            }catch (Throwable e){
                e.printStackTrace();
            }
        }
    }


}
