package tosamara.classifiers;

import tosamara.classifiers.xml.route.full.Route;

import java.util.HashMap;
import java.util.List;

public class RouteClassifier {
    public static HashMap<Integer, Route> routes;

    public static Route findById(Integer KR_ID){
        return routes.get(KR_ID);
    }

    public static Route findByNumber(String number, Route.TransportType type){
        for (Route r : routes.values()) {
            if (r.getNumber().equals(number) && r.getTransportType().equals(type)) {
                return r;
            }
        }
        return null;
    }

}
