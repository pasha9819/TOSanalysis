package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import static io.IOUtil.readFromBuffReader;

public class Request {
    private String url;

    Request(String url) {
        this.url = url;
    }

    public String getAnswer() {
        return javaRequest();
    }

    private String javaRequest(){
        try{
            URL u = new URL(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), Charset.forName("UTF-8")));
            return readFromBuffReader(br);
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String toString() {
        return "Request: " + url;
    }
}
