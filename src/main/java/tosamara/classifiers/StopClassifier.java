package tosamara.classifiers;

import tosamara.classifiers.parsers.StopParser;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.classifiers.xml.stop.Stops;

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
    private static HashMap<Integer, Stop> stopMap;

    /*
     * Load classifier from local data sources.
     */
    static {
        update();
        if (stopMap == null){
            System.err.println("Couldn't load StopClassifier from local sources");
            System.exit(-1);
        }
    }

    /**
     * Find Stop by his ID in classifier.
     * @param KS_ID stop ID
     * @return {@link Stop} or <code>null</code> if stop ID incorrect
     */
    public static Stop findById(Integer KS_ID){
        return stopMap.get(KS_ID);
    }

    /**
     * Update classifier from local data sources.
     */
    public static void update(){
        Stops stops = new StopParser().parseFromFile();
        HashMap<Integer, Stop> map = new HashMap<>();
        for (Stop stop : stops.getList()){
            map.put(stop.getKS_ID(), stop);
        }
        stopMap = map;
    }
}
