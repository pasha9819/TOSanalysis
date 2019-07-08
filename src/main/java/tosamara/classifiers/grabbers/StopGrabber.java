package tosamara.classifiers.grabbers;

import tosamara.Configuration;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.stop.Stops;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

public class StopGrabber extends Grabber{
    @Override
    protected String getURL() {
        return Configuration.STOPS_CLASSIFIER_URL;
    }

    @Override
    protected String getPath() {
        return Configuration.STOPS_CLASSIFIER_PATH;
    }

    @Override
    public void downloadAndUpdate() {
        try {
            Stops stops;
            try{
                String xml = downloadXml();
                stops = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), Stops.class);
                save(stops);
            }catch (Exception e){
                System.err.println("Couldn't load Stop info from tosamara.ru! [Data will be loaded from local sources]");
            }

            StopClassifier.update();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't update and load Stop info");
            System.exit(-1);
        }
    }
}