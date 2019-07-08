package tosamara.classifiers.grabbers;

import network.GetRequestBuilder;
import network.Request;

import javax.xml.bind.JAXB;
import java.io.File;

public abstract class Grabber {

    protected abstract String getURL();

    protected abstract String getPath();

    public abstract void downloadAndUpdate();

    protected String downloadXml() throws NoSuchMethodException {
        String url = getURL();
        if (url == null) {
            throw new NoSuchMethodException("getURL() is not defined");
        }
        Request r = new GetRequestBuilder(url).build();
        return r.getAnswer();
    }

    protected void save(Object xmlObject) throws NoSuchMethodException {
        String path = getPath();
        if (path == null) {
            throw new NoSuchMethodException("getPath() is not defined");
        }
        save(xmlObject, path);
    }

    protected void save(Object xmlObject, String path){
        JAXB.marshal(xmlObject, new File(path));
    }

}
