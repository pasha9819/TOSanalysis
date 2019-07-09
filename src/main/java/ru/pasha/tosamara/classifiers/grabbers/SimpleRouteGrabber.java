package ru.pasha.tosamara.classifiers.grabbers;

import ru.pasha.tosamara.Configuration;
import ru.pasha.tosamara.classifiers.xml.route.simple.SimpleRoutes;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;

public class SimpleRouteGrabber extends Grabber {
    @Override
    protected String getURL() {
        return Configuration.SIMPLE_ROUTES_URL;
    }

    @Override
    protected String getPath() {
        return Configuration.SIMPLE_ROUTES_PATH;
    }

    @Override
    public void downloadAndUpdate() {
        try {
            String xml = downloadXml();
            SimpleRoutes s = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes()), SimpleRoutes.class);
            save(s);
        } catch (NoSuchMethodException e) {
            System.err.println("Couldn't load SimpleRoutes info from tosamara.ru!");
            e.printStackTrace();
        }
    }
}