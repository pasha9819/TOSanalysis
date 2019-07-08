package tosamara.classifiers;

import tosamara.classifiers.parsers.RouteParser;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.route.full.Routes;

import java.util.HashMap;

public class RouteClassifier {
    private static HashMap<Integer, Route> routeMap;
    static {
        update();
        if (routeMap == null){
            System.err.println("Couldn't load RouteClassifier from local sources");
            System.exit(-1);
        }
    }

    public static Route findById(Integer KR_ID){
        return routeMap.get(KR_ID);
    }

    public static void update(){
        Routes routes = new RouteParser().parseFromFile();
        HashMap<Integer, Route> map = new HashMap<>();

        for (Route route : routes.getList()){
            // 1 == MUNICIPAL
            if (route.getAffiliationID() == 1){
                map.put(route.getKR_ID(), route);
            }
        }
        routeMap = map;
    }

}
