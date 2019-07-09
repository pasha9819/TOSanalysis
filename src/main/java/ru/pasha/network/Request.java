package ru.pasha.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import static ru.pasha.io.IOUtil.readFromBuffReader;

/**
 * Encapsulates HTTP GET request
 */
public class Request {
    /**
     * Request URL
     */
    private String url;

    /**
     * Create a <code>Request</code> object from the String representation.
     * @param url the String to parse as a URL
     */
    Request(String url) {
        this.url = url;
    }

    /**
     * Send request and read answer.
     * @return server response
     */
    public String getAnswer() {
        try{
            URL u = new URL(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), Charset.forName("UTF-8")));
            return readFromBuffReader(br);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String toString() {
        return "Request: " + url;
    }
}
