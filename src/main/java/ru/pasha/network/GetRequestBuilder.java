package ru.pasha.network;

/**
 * Class for request building. This class helps to create a GET request to the server.
 */
public class GetRequestBuilder {
    /**
     * Encapsulates string with server URL and sent parameters.
     */
    private StringBuilder urlBuilder;

    /**
     * Flag for create request. If count of parameters = 0, then paramEmpty = <code>true</code>,
     * when you add first parameter to request, paramEmpty will be equal <code>false</code>.
     * The flag is required for correct query creation.
     */
    private boolean paramEmpty = true;

    /**
     * Prepare request to <code>serverAddress</code> without any parameters
     * @param serverAddress URL for sending parameters
     */
    public GetRequestBuilder(String serverAddress) {
        urlBuilder = new StringBuilder(serverAddress);
    }

    /**
     * Append <code>param=value</code> to request-text
     * @param param parameter name
     * @param value parameter value
     * @return current request builder
     */
    public GetRequestBuilder appendParam(String param, Object value){
        if (paramEmpty){
            paramEmpty = false;
        }else {
            urlBuilder.append('&');
        }
        urlBuilder.append(param).append('=').append(value);
        return this;
    }

    /**
     * Creates a <code>Request</code> instance based on the current configuration
     * @return instance of <code>Request</code> configured with the parameters currently set in this builder
     */
    public Request build() {
        return new Request(urlBuilder.toString());
    }

}
