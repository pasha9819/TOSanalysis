package tosamara.classifiers.grabbers;

import tosamara.Configuration;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.parsers.StopParser;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.classifiers.xml.stop.Stops;

import javax.xml.bind.JAXB;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

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
    public void updateAndLoad() {
        try {
            Stops stops;
            try{
                String xml = downloadXml();
                stops = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), Stops.class);
                save(stops);
            }catch (Exception e){
                System.err.println("Couldn't load Stop info from tosamara.ru! [Data was loaded from local sources]");
                stops = new StopParser().parseFromFile();
            }

            HashMap<Integer, Stop> stopMap = new HashMap<>();
            for (Stop stop : stops.getList()){
                stopMap.put(stop.getKS_ID(), stop);
            }
            StopClassifier.stops = stopMap;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't update and load Stop info");
            System.exit(-1);
        }
    }
}