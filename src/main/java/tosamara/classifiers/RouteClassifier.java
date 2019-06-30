package tosamara.classifiers;

import tosamara.classifiers.xml.route.full.Route;

import java.util.List;

public class RouteClassifier {
    public static List<Route> routes;

    public static Route findById(Integer KR_ID){
        int index = -1;
        for (int i = 0; i < routes.size(); i++)
            if (routes.get(i).getKR_ID().equals(KR_ID))
                index = i;
        if (index == -1)
            return null;
        return routes.get(index);
    }

    public static Route findByNumber(String number, Route.TransportType type){
        int index = -1;
        for (int i = 0; i < routes.size(); i++)
            if (routes.get(i).getNumber().equals(number) && routes.get(i).getTransportType().equals(type))
                index = i;
        if (index == -1)
            return null;
        return routes.get(index);
    }

}
