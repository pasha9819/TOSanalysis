package tosamara.classifiers.grabbers;

import io.IOUtil;
import tosamara.Configuration;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.route.full.RouteList;
import tosamara.classifiers.xml.stop.StopList;

import javax.xml.bind.JAXB;
import java.io.*;
import java.nio.charset.Charset;

public class StopGrabber extends Grabber{
    @Override
    String getURL() {
        return Configuration.STOPS_CLASSIFIER_URL;
    }

    @Override
    String getPath() {
        return Configuration.STOPS_CLASSIFIER_PATH;
    }

    @Override
    public void update() {
        try {
            String xml = downloadXml();
            StopList s = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), StopList.class);
            save(s);
            try(BufferedReader r = new BufferedReader(new InputStreamReader(
                    new FileInputStream(getPath()), Charset.forName("UTF-8")))){
                String xml1 = IOUtil.readFromBuffReader(r);
                StopList s1 = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), StopList.class);

                StopClassifier.stops = s.getStops();
            }catch (IOException e){
                System.err.println("StopClassifier doesn't load");
                System.exit(-1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
