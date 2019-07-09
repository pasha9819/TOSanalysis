package ru.pasha.tosamara.methods;

import ru.pasha.tosamara.classifiers.xml.stop.Stop;
import ru.pasha.tosamara.methods.json.ArrivalToStop;
import ru.pasha.tosamara.methods.json.Transport;

import java.util.List;

/**
 * Wrap for ToSamara API
 */
public abstract class API {
    /**
     * Server address for sending requests
     */
    public static final String SERVER_ADDRESS = "http://www.tosamara.ru/api/json?";

    /**
     * @see GetFirstArrivalToStop
     */
    public static List<ArrivalToStop> getFirstArrivalToStop(Stop stop, int count){
        return new GetFirstArrivalToStop(stop.getId(), count).execute();
    }

    /**
     * @see GetFirstArrivalToStop
     */
    public static List<ArrivalToStop> getFirstArrivalToStop(Stop stop){
        return getFirstArrivalToStop(stop, 100);
    }

    /**
     * @see GetSurroundingTransports
     */
    public static List<Transport> getSurroundingTransports(double latitude, double longitude){
        return getSurroundingTransports(latitude, longitude, 80, 100);
    }

    /**
     * @see GetSurroundingTransports
     */
    public static List<Transport> getSurroundingTransports(double latitude, double longitude, int radius, int count){
        return new GetSurroundingTransports(latitude, longitude, radius, count).execute();
    }
}
