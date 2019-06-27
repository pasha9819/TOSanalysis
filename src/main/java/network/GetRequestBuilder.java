package network;

public class GetRequestBuilder {
    private StringBuilder urlBuilder;
    private boolean paramEmpty = true;

    public GetRequestBuilder(String serverAddress) {
        urlBuilder = new StringBuilder(serverAddress);
    }

    public GetRequestBuilder appendParam(String param, Object value){
        if (paramEmpty){
            paramEmpty = false;
        }else {
            urlBuilder.append('&');
        }
        urlBuilder.append(param).append('=').append(value);
        return this;
    }

    public Request build() {
        return new Request(urlBuilder.toString());
    }

}
