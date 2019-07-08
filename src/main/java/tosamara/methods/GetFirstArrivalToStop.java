package tosamara.methods;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import network.GetRequestBuilder;
import network.Request;
import tosamara.classifiers.StopClassifier;
import tosamara.classifiers.xml.stop.Stop;
import tosamara.methods.json.ArrivalToStop;
import tosamara.util.TokenGenerator;

import java.util.ArrayList;
import java.util.List;

class GetFirstArrivalToStop implements Method {
    private Integer KS_ID;
    private Integer count;

    GetFirstArrivalToStop(Integer KS_ID, Integer count) {
        this.KS_ID = KS_ID;
        this.count = count;
    }

    @Override
    public String secretKey() {
        return TokenGenerator.getToken(KS_ID, count);
    }

    List<ArrivalToStop> execute() {
        Request r = new GetRequestBuilder(API.SERVER_ADDRESS)
                .appendParam("method", "getFirstArrivalToStop")
                .appendParam("KS_ID", KS_ID)
                .appendParam("COUNT", count)
                .appendParam("os", "android")
                .appendParam("clientid", "BelyaevPI")
                .appendParam("authkey", secretKey())
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
                    a.setNextStopId(stop.getKS_ID());
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
