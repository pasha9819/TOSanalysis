package ru.pasha.tosamara.methods;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import ru.pasha.network.GetRequestBuilder;
import ru.pasha.network.Request;
import ru.pasha.tosamara.classifiers.StopClassifier;
import ru.pasha.tosamara.classifiers.xml.stop.Stop;
import ru.pasha.tosamara.methods.json.ArrivalToStop;
import ru.pasha.tosamara.util.TokenGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates call to server getFirstArrivalToStop method
 */
class GetFirstArrivalToStop implements Method {
    private Integer id;
    private Integer count;

    GetFirstArrivalToStop(int id, int count) {
        this.id = id;
        this.count = count;
    }

    @Override
    public String authKey() {
        return TokenGenerator.getToken(id, count);
    }

    List<ArrivalToStop> execute() {
        Request r = new GetRequestBuilder(API.SERVER_ADDRESS)
                .appendParam("method", "getFirstArrivalToStop")
                .appendParam("KS_ID", id)
                .appendParam("COUNT", count)
                .appendParam("os", "android")
                .appendParam("clientid", "BelyaevPI")
                .appendParam("authkey", authKey())
                .build();

        String answer = r.getAnswer();
        try{
            ListWrapper wrapper = new Gson().fromJson(answer, ListWrapper.class);
            for (ArrivalToStop a : wrapper.getArrivalList()){
                Stop stop;
            /*
                If transport near the previous stop, then change next stop to previous stop
                it is need, because forecast are late and stops - it is point on map,
                but public transport is not point.
                Also, average update of data on ToSamaraServer = 30 sec, and transport can
                drive the stop in less than 30 seconds.
             */
                if (a.getSpanLength() - a.getRemainingLength() < 51){
                    stop = a.getPrevStop();
                    a.setRemainingLength(a.getRemainingLength() - a.getSpanLength());
                    a.setNextStopId(stop.getId());
                }else {
                    stop = StopClassifier.findById(a.getNextStopId());
                }
                a.setStop(stop);
            }
            return wrapper.getArrivalList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    private static class ListWrapper{
        @SerializedName("arrival")
        private List<ArrivalToStop> arrivalList;

        List<ArrivalToStop> getArrivalList() {
            return arrivalList;
        }
    }
}
