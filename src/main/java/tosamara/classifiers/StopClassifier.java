package tosamara.classifiers;

import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.stop.Stop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static tosamara.classifiers.xml.route.full.Route.TransportType.BUS;
import static tosamara.classifiers.xml.route.full.Route.TransportType.TRAM;
import static tosamara.classifiers.xml.route.full.Route.TransportType.TROL;

public class StopClassifier {
    public static HashMap<Integer, Stop> stops;

    public static Stop findById(Integer KS_ID){
        return stops.get(KS_ID);
    }

    public static List<Route> getRoutesByStopId(Integer KS_ID){
        List<Route> routes = new ArrayList<>();
        Stop stop = StopClassifier.findById(KS_ID);
        if (stop == null){
            return routes;
        }
        String[] busNumbers = stop.getBusesMunicipal().split(", ");
        String[] tramNumbers = stop.getTrams().split(", ");
        String[] trolNumbers = stop.getTrolleybuses().split(", ");

        String[][] numbers = {busNumbers, tramNumbers, trolNumbers};
        Route.TransportType[] types = {BUS, TRAM, TROL};

        for (int i = 0; i < numbers.length; i++) {
            for(String s : numbers[i]){
                Route r = RouteClassifier.findByNumber(s, types[i]);
                if (r != null){
                    routes.add(r);
                }
            }
        }
        return routes;
    }
}
