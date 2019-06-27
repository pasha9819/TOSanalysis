package tosamara.classifiers.parsers;

import tosamara.Configuration;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.classifiers.xml.stop.StopList;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.List;

public class StopParser extends Parser<Stop> {
    @Override
    protected String getPath() {
        return Configuration.STOPS_CLASSIFIER_PATH;
    }

    public List<Stop> parse(){
        try{
            StopList s = JAXB.unmarshal(new File(getPath()), StopList.class);
            return s.getStops();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
