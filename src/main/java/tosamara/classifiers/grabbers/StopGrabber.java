package tosamara.classifiers.grabbers;

import io.IOUtil;
import tosamara.Configuration;
import tosamara.classifiers.RouteClassifier;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.route.full.RouteList;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.classifiers.xml.stop.StopList;

import javax.xml.bind.JAXB;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

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
            String xml;
            try{
                xml = downloadXml();
                StopList stopList = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), StopList.class);
                save(stopList);
            }catch (Exception e){
                e.printStackTrace();
            }
            try(BufferedReader r = new BufferedReader(new InputStreamReader(
                    new FileInputStream(getPath()), Charset.forName("UTF-8")))){
                xml = IOUtil.readFromBuffReader(r);
                StopList s = JAXB.unmarshal(new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))), StopList.class);

                HashMap<Integer, Stop> stopMap = new HashMap<>();
                for (Stop stop : s.getStops()){
                    stopMap.put(stop.getKS_ID(), stop);
                }
                StopClassifier.stops = stopMap;
            }catch (IOException e){
                System.err.println("StopClassifier doesn't load");
                System.exit(-1);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
