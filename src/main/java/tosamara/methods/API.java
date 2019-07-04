package tosamara.methods;


import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.json.ArrivalToStop;
import tosamara.methods.json.Transport;

import java.util.List;

public abstract class API {
    public static final String SERVER_ADDRESS = "http://www.tosamara.ru/api/json?";

    public static List<ArrivalToStop> getFirstArrivalToStop(Stop stop, int count){
        return new GetFirstArrivalToStop(stop.getKS_ID(), count).execute();
    }

    public static List<ArrivalToStop> getFirstArrivalToStop(Stop stop){
        return getFirstArrivalToStop(stop, 100);
    }

    public static List<Transport> getSurroundingTransports(Double latitude, Double longitude){
        return getSurroundingTransports(latitude, longitude, 50, 100);
    }

    public static List<Transport> getSurroundingTransports(Double latitude, Double longitude, Integer radius, Integer count){
        return new GetSurroundingTransports(latitude, longitude, radius, count).execute();
    }
}
