package tosamara.classifiers.grabbers;

import tosamara.Configuration;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.parsers.RouteParser;
import tosamara.classifiers.parsers.SimpleRouteParser;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.route.full.Routes;
import tosamara.classifiers.xml.route.simple.SimpleRoute;
import tosamara.classifiers.xml.route.simple.SimpleRoutes;

import javax.xml.bind.JAXB;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

public class RouteGrabber extends Grabber {
    @Override
    protected String getURL() {
        return Configuration.ROUTES_CLASSIFIER_URL;
    }

    @Override
    protected String getPath() {
        return Configuration.ROUTES_CLASSIFIER_PATH;
    }

    @Override
    public void updateAndLoad() {
        try {
            new SimpleRouteGrabber().updateAndLoad();
            SimpleRoutes simpleList = new SimpleRouteParser().parseFromFile();

            Routes routes;
            try{
                String xml = downloadXml();
                routes = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), Routes.class);
            }catch (Exception e){
                System.err.println("Couldn't load Routes info from tosamara.ru! [Data was loaded from local sources]");
                routes = new RouteParser().parseFromFile();
            }

            HashMap<Integer, Route> routeMap = new HashMap<>();

            for (Route route : routes.getList()){
                SimpleRoute s = simpleList.getByKR_ID(route.getKR_ID());
                route.setAffiliationID(s.getAffiliationID());
                // 1 == MUNICIPAL
                if (route.getAffiliationID() == 1){
                    routeMap.put(route.getKR_ID(), route);
                }
            }
            save(routes);
            RouteClassifier.routes = routeMap;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't update and load Route info");
            System.exit(-1);
        }
    }
}