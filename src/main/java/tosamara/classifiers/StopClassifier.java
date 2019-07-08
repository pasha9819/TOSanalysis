package tosamara.classifiers;

import tosamara.classifiers.parsers.StopParser;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.classifiers.xml.stop.Stops;

import java.util.HashMap;

public class StopClassifier {
    private static HashMap<Integer, Stop> stopMap;
    static {
        update();
        if (stopMap == null){
            System.err.println("Couldn't load StopClassifier from local sources");
            System.exit(-1);
        }
    }

    public static Stop findById(Integer KS_ID){
        return stopMap.get(KS_ID);
    }

    public static void update(){
        Stops stops = new StopParser().parseFromFile();
        HashMap<Integer, Stop> map = new HashMap<>();
        for (Stop stop : stops.getList()){
            map.put(stop.getKS_ID(), stop);
        }
        stopMap = map;
    }
}
