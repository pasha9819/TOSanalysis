package ru.pasha.tosamara.classifiers;

import ru.pasha.tosamara.classifiers.parsers.StopParser;
import ru.pasha.tosamara.classifiers.xml.stop.Stop;
import ru.pasha.tosamara.classifiers.xml.stop.Stops;

import java.util.HashMap;

/**
 * Stop Classifier of Samara public transport.
 * Encapsulates HashMap: <code>{stopId : Stop}</code>
 * @see Stop
 */
public class StopClassifier {
    /**
     * Map, which storing Stops and their ID.
     * <p>Key: stop ID</p>
     * <p>Value: {@link Stop}</p>
     */
    private static HashMap<Integer, Stop> STOP_MAP;

    /*
     * Load classifier from local data sources.
     */
    static {
        update();
        if (STOP_MAP == null){
            System.err.println("Couldn't load StopClassifier from local sources");
            System.exit(1);
        }
    }

    /**
     * Find Stop by his ID in classifier.
     * @param id stop ID
     * @return {@link Stop} or <code>null</code> if stop ID incorrect
     */
    public static Stop findById(int id){
        return STOP_MAP.get(id);
    }

    /**
     * Update classifier from local data sources.
     */
    public static void update(){
        Stops stops = new StopParser().parseFromFile();
        HashMap<Integer, Stop> map = new HashMap<>();
        for (Stop stop : stops.getList()){
            map.put(stop.getId(), stop);
        }
        STOP_MAP = map;
    }
}
