package tosamara.classifiers.parsers;

import tosamara.Configuration;
import tosamara.classifiers.xml.route.full.Routes;

import javax.xml.bind.JAXB;
import java.io.File;

public class RouteParser extends Parser<Routes> {
    @Override
    protected String getPath() {
        return Configuration.ROUTES_CLASSIFIER_PATH;
    }

    @Override
    public Routes parseFromFile() {
        try{
            return JAXB.unmarshal(new File(getPath()), Routes.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
