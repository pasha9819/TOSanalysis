package ru.pasha.tosamara.classifiers.parsers;

import ru.pasha.tosamara.Configuration;
import ru.pasha.tosamara.classifiers.xml.route.full.Routes;

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
            // TODO: 09.07.2019 убрать пробел
            return JAXB.unmarshal(new File(getPath()), Routes.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
