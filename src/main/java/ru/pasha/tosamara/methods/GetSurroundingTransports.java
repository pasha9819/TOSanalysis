package ru.pasha.tosamara.methods;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import ru.pasha.network.GetRequestBuilder;
import ru.pasha.network.Request;
import ru.pasha.tosamara.methods.json.Transport;
import ru.pasha.tosamara.util.TokenGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates call to server getSurroundingTransport method
 */
class GetSurroundingTransports implements Method {
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private Integer count;

    GetSurroundingTransports(Double latitude, Double longitude, Integer radius, Integer count) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.count = count;
    }

    @Override
    public String authKey() {
        return TokenGenerator.getToken(latitude, longitude, radius, count);
    }

    List<Transport> execute() {
        Request r = new GetRequestBuilder(API.SERVER_ADDRESS)
                .appendParam("method", "getSurroundingTransports")
                .appendParam("LATITUDE", latitude)
                .appendParam("LONGITUDE", longitude)
                .appendParam("RADIUS", radius)
                .appendParam("COUNT", count)
                .appendParam("os", "android")
                .appendParam("clientid", "BelyaevPI")
                .appendParam("authkey", authKey())
                .build();
        String answer = r.getAnswer();
        try{
            GetSurroundingTransports.ListWrapper wrapper = new Gson().fromJson(answer, GetSurroundingTransports.ListWrapper.class);
            return wrapper.getTransportList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    private static class ListWrapper{
        @SerializedName("transports")
        private List<Transport> transportList;

        List<Transport> getTransportList() {
            return transportList;
        }
    }
}
