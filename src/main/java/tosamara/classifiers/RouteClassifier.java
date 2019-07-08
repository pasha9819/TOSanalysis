package tosamara.classifiers;

import tosamara.classifiers.parsers.RouteParser;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.route.full.Routes;

import java.util.HashMap;

/**
 * Route Classifier of Samara public transport.
 * Encapsulates HashMap: <code>{routeId : Route}</code>
 * @see Route
 */
public class RouteClassifier {
    /**
     * Map, which storing Routes and their ID.
     * <p>Key: route ID</p>
     * <p>Value: {@link Route}</p>
     */
    private static HashMap<Integer, Route> routeMap;

    /*
     * Load classifier from local data sources.
     */
    static {
        update();
        if (routeMap == null){
            System.err.println("Couldn't load RouteClassifier from local sources");
            System.exit(-1);
        }
    }

    /**
     * Find Route by his ID in classifier.
     * @param KR_ID route ID
     * @return {@link Route} or <code>null</code> if route ID incorrect
     */
    public static Route findById(Integer KR_ID){
        return routeMap.get(KR_ID);
    }

    /**
     * Update classifier from local data sources.
     * If route is not municipal, it not it is not entered into classifier.
     */
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
