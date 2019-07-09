package ru.pasha.tosamara.classifiers;

import ru.pasha.tosamara.classifiers.parsers.RouteParser;
import ru.pasha.tosamara.classifiers.xml.route.full.Route;
import ru.pasha.tosamara.classifiers.xml.route.full.Routes;

import java.util.HashMap;

/**
 * Route Classifier of Samara public transport.
 * Encapsulates HashMap: <code>{routeId : Route}</code>
 * @see Route
 */
public class RouteClassifier {
    private static final int MUNICIPAL = 1;
    /**
     * Map, which storing Routes and their ID.
     * <p>Key: route ID</p>
     * <p>Value: {@link Route}</p>
     */
    private static HashMap<Integer, Route> ROUTE_MAP;

    /*
     * Load classifier from local data sources.
     */
    static {
        update();
        if (ROUTE_MAP == null){
            System.err.println("Couldn't load RouteClassifier from local sources");
            System.exit(1);
        }
    }

    /**
     * Find Route by his ID in classifier.
     * @param id route ID
     * @return {@link Route} or <code>null</code> if route ID incorrect
     */
    public static Route findById(int id){
        return ROUTE_MAP.get(id);
    }

    /**
     * Update classifier from local data sources.
     * If route is not municipal, it not it is not entered into classifier.
     */
    public static void update(){
        Routes routes = new RouteParser().parseFromFile();
        HashMap<Integer, Route> map = new HashMap<>();

        for (Route route : routes.getList()){
            if (MUNICIPAL == route.getAffiliationID()){
                map.put(route.getId(), route);
            }
        }
        ROUTE_MAP = map;
    }

}
