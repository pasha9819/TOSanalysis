package tosamara.classifiers.parsers;

import tosamara.Configuration;
import tosamara.classifiers.xml.route.simple.SimpleRoutes;

import javax.xml.bind.JAXB;
import java.io.File;

public class SimpleRouteParser extends Parser<SimpleRoutes> {
    @Override
    protected String getPath() {
        return Configuration.SIMPLE_ROUTES_PATH;
    }

    @Override
    public SimpleRoutes parseFromFile() {
        try{
            return JAXB.unmarshal(new File(getPath()), SimpleRoutes.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
}
}
