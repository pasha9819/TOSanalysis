package tosamara.methods;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import network.Request;
import network.GetRequestBuilder;
import tosamara.util.TokenGenerator;
import tosamara.methods.json.ArrivalToStop;

import java.util.List;

public class GetFirstArrivalToStop implements Method {
    private Integer KS_ID;
    private Integer count;

    public GetFirstArrivalToStop(Integer KS_ID, Integer count) {
        this.KS_ID = KS_ID;
        this.count = count;
    }

    @Override
    public String secretKey() {
        return TokenGenerator.getToken(KS_ID, count);
    }

    public List<ArrivalToStop> execute() {
        Request r = new GetRequestBuilder(API.SERVER_ADDRESS)
                .appendParam("method", "getFirstArrivalToStop")
                .appendParam("KS_ID", KS_ID)
                .appendParam("COUNT", count)
                .appendParam("os", "android")
                .appendParam("clientid", "BelyaevPI")
                .appendParam("authkey", secretKey())
                .build();
        //long start = System.currentTimeMillis();
        String answer = r.getAnswer();
        //System.out.println(System.currentTimeMillis() - start);
        ListWrapper wrapper = new Gson().fromJson(answer, ListWrapper.class);
        return wrapper.getArrivalList();
    }

    private static class ListWrapper{
        @SerializedName("arrival")
        private List<ArrivalToStop> arrivalList;

        List<ArrivalToStop> getArrivalList() {
            return arrivalList;
        }

        public ListWrapper setArrivalList(List<ArrivalToStop> arrivalList) {
            this.arrivalList = arrivalList;
            return this;
        }
    }
}
