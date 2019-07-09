package ru.pasha.tosamara.classifiers.grabbers;

import ru.pasha.network.GetRequestBuilder;
import ru.pasha.network.Request;

import javax.xml.bind.JAXB;
import java.io.File;

/**
 * Abstract class helps fetch data from URL and save it to local machine.
 */
public abstract class Grabber {
    /**
     * Get URL of remote data source.
     * @return URL as string
     */
    protected abstract String getURL();

    /**
     * Get path of local data source.
     * @return path of local data source as string
     */
    protected abstract String getPath();

    /**
     * Download data from remote server, save it to local data source and update Classifier.
     */
    public abstract void downloadAndUpdate();

    /**
     * Download XML-text from remote server.
     * @return XML-text
     * @throws NoSuchMethodException if {@link #getURL()} is not defined in child class
     */
    protected String downloadXml() throws NoSuchMethodException {
        String url = getURL();
        if (url == null) {
            throw new NoSuchMethodException("getURL() is not defined");
        }
        Request r = new GetRequestBuilder(url).build();
        return r.getAnswer();
    }

    /**
     * Save XML-object to local machine.
     * @param xmlObject saved object
     * @throws NoSuchMethodException if {@link #getPath()}  is not defined in child class
     */
    protected void save(Object xmlObject) throws NoSuchMethodException {
        String path = getPath();
        if (path == null) {
            throw new NoSuchMethodException("getPath() is not defined");
        }
        save(xmlObject, path);
    }

    /**
     * Save XML-object to any file at local machine.
     * @param xmlObject saved object
     * @param path pathname(filename) to save
     */
    protected void save(Object xmlObject, String path){
        JAXB.marshal(xmlObject, new File(path));
    }

}
