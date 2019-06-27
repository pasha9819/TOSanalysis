package tosamara.classifiers.grabbers;

import tosamara.Configuration;
import tosamara.classifiers.xml.route.simple.SimpleRouteList;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;

public class SimpleRouteGrabber extends Grabber {
    @Override
    String getURL() {
        return Configuration.SIMPLE_ROUTES_URL;
    }

    @Override
    String getPath() {
        return Configuration.SIMPLE_ROUTES_PATH;
    }

    @Override
    public void update() {}

    SimpleRouteList download() throws NoSuchMethodException {
        String xml = downloadXml();
        SimpleRouteList s = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes()), SimpleRouteList.class);
        save(s);
        return s;
    }
}
