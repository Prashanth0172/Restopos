package com.hyva.restopos.util;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;

import java.io.IOException;
import java.util.Map;

/**
 * Created by bpradeep on 02-07-2017.
 */
public abstract class BaseNetwork<T> extends AsyncCompletionHandler {


    private AsyncHttpClient client;

    private AsyncHttpClient.BoundRequestBuilder builder;

    public enum CommunicationType {
        get, post, delete, put
    }

    private CommunicationType communicationType;

    private String requestUrl;

    private Map<String, String> headers;

    private Map<String, String> parameters;

    private Map<String, String> queryParameters;

    private String body;

    private byte[] bodyBytes;

    private String bodyEncoding=null;

    private boolean isError;

    private String errorMessage;

    public ListenableFuture<T> execute() throws IOException {

        AsyncHttpClientConfig.Builder asyncHttpClientConfigBuilder = new AsyncHttpClientConfig.Builder();
        asyncHttpClientConfigBuilder
                .setConnectionTimeoutInMs(5*60*1000)
//                .setRequestTimeoutInMs(5*6*1000)
                .setRequestTimeoutInMs(30*6*1000)
                .setMaxConnectionLifeTimeInMs(5*60*1000)
                .setIdleConnectionTimeoutInMs(5*60*1000)
                .setIdleConnectionInPoolTimeoutInMs(5*60*1000);

        client = new AsyncHttpClient(asyncHttpClientConfigBuilder.build());

        switch (communicationType) {
            case get:
                builder = client.prepareGet(requestUrl);
                break;
            case post:
                builder = client.preparePost(requestUrl);
                break;
            case delete:
                builder = client.prepareDelete(requestUrl);
                break;
            case put:
                builder = client.preparePut(requestUrl);
                break;
        }

        if (headers != null) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        if (parameters != null) {
            for (String key : parameters.keySet()) {
                builder.addParameter(key, parameters.get(key));
            }
        }

        if (queryParameters != null) {
            for (String key : queryParameters.keySet()) {
                builder.addQueryParameter(key, queryParameters.get(key));
            }
        }

        if(body!=null)
            builder.setBody(body);

        if(bodyBytes != null)
            builder.setBody(bodyBytes);

        if(bodyEncoding!=null)
            builder.setBodyEncoding(bodyEncoding);

        return builder.execute(this);
    }

    @Override
    public void onThrowable(Throwable t) {
        super.onThrowable(t);
        isError = true;
        errorMessage = t.getMessage();
    }

    //------------------------------------------------------------------------------------------------
    // Getters and Setters
    //------------------------------------------------------------------------------------------------


    public AsyncHttpClient getClient() {
        return client;
    }

    public AsyncHttpClient.BoundRequestBuilder getBuilder() {
        return builder;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setCommunicationType(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    public void setBodyBytes(byte[] bodyBytes) {
        this.bodyBytes = bodyBytes;
    }

    public void setBodyEncoding(String encoding){
        bodyEncoding = encoding;
    }
}