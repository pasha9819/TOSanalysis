package tosamara.methods;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import network.GetRequestBuilder;
import network.Request;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.json.ArrivalToStop;
import tosamara.methods.json.Transport;
import tosamara.util.TokenGenerator;

import java.util.List;

public class GetSurroundingTransports implements Method {
    private Double latitude;
    private Double longitude;
    private Integer radius;
    private Integer count;

    public GetSurroundingTransports(Double latitude, Double longitude, Integer radius, Integer count) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.count = count;
    }

    @Override
    public String secretKey() {
        return TokenGenerator.getToken(latitude, longitude, radius, count);
    }

    public List<Transport> execute() {
        Request r = new GetRequestBuilder(API.SERVER_ADDRESS)
                .appendParam("method", "getSurroundingTransports")
                .appendParam("LATITUDE", latitude)
                .appendParam("LONGITUDE", longitude)
                .appendParam("RADIUS", radius)
                .appendParam("COUNT", count)
                .appendParam("os", "android")
                .appendParam("clientid", "BelyaevPI")
                .appendParam("authkey", secretKey())
                .build();
        String answer = r.getAnswer();
        GetSurroundingTransports.ListWrapper wrapper = new Gson().fromJson(answer, GetSurroundingTransports.ListWrapper.class);
        return wrapper.getTransportList();
    }

    private static class ListWrapper{
        @SerializedName("transports")
        private List<Transport> transportList;

        public List<Transport> getTransportList() {
            return transportList;
        }

        public ListWrapper setTransportList(List<Transport> transportList) {
            this.transportList = transportList;
            return this;
        }
    }
}
