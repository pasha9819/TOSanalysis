package ru.pasha.tosamara.classifiers.grabbers;

import org.springframework.boot.SpringApplication;
import ru.pasha.tosamara.Configuration;
import ru.pasha.tosamara.classifiers.RouteClassifier;
import ru.pasha.tosamara.classifiers.parsers.RouteParser;
import ru.pasha.tosamara.classifiers.parsers.SimpleRouteParser;
import ru.pasha.tosamara.classifiers.xml.route.full.Route;
import ru.pasha.tosamara.classifiers.xml.route.full.Routes;
import ru.pasha.tosamara.classifiers.xml.route.simple.SimpleRoute;
import ru.pasha.tosamara.classifiers.xml.route.simple.SimpleRoutes;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

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
    public void downloadAndUpdate() {
        try {
            new SimpleRouteGrabber().downloadAndUpdate();
            SimpleRoutes simpleList = new SimpleRouteParser().parseFromFile();

            Routes routes;
            try{
                String xml = downloadXml();
                routes = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), Routes.class);
            }catch (Exception e){
                System.err.println("Couldn't load Routes info from tosamara.ru! [Data was loaded from local sources]");
                routes = new RouteParser().parseFromFile();
            }

            for (Route route : routes.getList()){
                SimpleRoute s = simpleList.getById(route.getId());
                route.setAffiliationID(s.getAffiliationID());
            }
            save(routes);
            RouteClassifier.update();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't download and update Route info");
            System.exit(1);
        }
    }
}