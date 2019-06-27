package tosamara.classifiers.grabbers;

import io.IOUtil;
import tosamara.Configuration;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.xml.route.full.LastStops;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.route.full.RouteList;
import tosamara.classifiers.xml.route.simple.SimpleRoute;
import tosamara.classifiers.xml.route.simple.SimpleRouteList;

import javax.xml.bind.JAXB;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RouteGrabber extends Grabber {
    @Override
    String getURL() {
        return Configuration.ROUTES_CLASSIFIER_URL;
    }

    @Override
    String getPath() {
        return Configuration.ROUTES_CLASSIFIER_PATH;
    }

    @Override
    public void update() {
        try {
            RouteList routeList;
            /*SimpleRouteList simpleList = new SimpleRouteGrabber().download();

            String xml = downloadXml();
            routeList = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes()), RouteList.class);

            for (Route route : routeList.getRoutes()){
                SimpleRoute s = simpleList.getByKR_ID(route.getKR_ID());
                route.setAffiliationID(s.getAffiliationID());
            }

            save(routeList);


            LastStops ls = new LastStops();
            HashSet<Route.Stop> uniqueStops = new HashSet<>();

            for(Route r : routeList.getRoutes()){
                // TODO: 10.06.2019 1 == MUNICIPAL !!!
                if (!r.getAffiliationID().equals(1)){
                    continue;
                }
                List<Route.Stop> routeStops = r.getStops();
                Route.Stop last = routeStops.get(routeStops.size() - 1);
                uniqueStops.add(last);
            }

            ls.setStops(new ArrayList<>(uniqueStops));

            save(ls, Configuration.LAST_STOPS_CLASSIFIER_PATH);*/
            try(BufferedReader r = new BufferedReader(new InputStreamReader(
                    new FileInputStream(getPath()), Charset.forName("UTF-8")))){
                String xml = IOUtil.readFromBuffReader(r);
                routeList = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), RouteList.class);
                RouteClassifier.routes = routeList.getRoutes();
            }catch (IOException e){
                System.err.println("RouteClassifier doesn't load");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
