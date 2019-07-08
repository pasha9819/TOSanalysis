package network;

import org.junit.Test;
import tosamara.methods.API;

import static org.junit.Assert.*;

public class GetRequestBuilderTest {

    @Test
    public void serverAddress(){
        GetRequestBuilder builder = new GetRequestBuilder(API.SERVER_ADDRESS);
        Request r = builder.build();
        assertTrue(r.toString().contains("tosamara.ru"));
    }

    @Test
    public void appendParam() {
        GetRequestBuilder builder = new GetRequestBuilder(API.SERVER_ADDRESS);
        builder.appendParam("param1", "value1");
        Request r = builder.build();
        assertTrue(r.toString().contains("param1=value1"));
    }
}