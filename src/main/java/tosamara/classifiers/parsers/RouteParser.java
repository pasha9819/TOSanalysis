package tosamara.classifiers.parsers;

import tosamara.Configuration;
import tosamara.classifiers.xml.route.full.Route;
import tosamara.classifiers.xml.route.full.RouteList;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.List;

public class RouteParser extends Parser<Route> {
    @Override
    protected String getPath() {
        return Configuration.ROUTES_CLASSIFIER_PATH;
    }

    @Override
    public List<Route> parse() {
        try{
            RouteList s = JAXB.unmarshal(new File(getPath()), RouteList.class);
            return s.getRoutes();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
