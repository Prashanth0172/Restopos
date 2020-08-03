package com.hyva.restopos.util;

import com.ning.http.client.Response;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Created by bpradeep on 02-07-2017.
 */
public class PerformCommunication extends BaseNetwork<Response>{

    protected static Logger log = Logger.getLogger(PerformCommunication.class);

    public PerformCommunication(byte[] data,
                                String requestUrl,
                                HashMap<String, String> queryParameters,
                                HashMap<String, String> headers,
                                CommunicationType verb) {
        super.setCommunicationType(verb);
        super.setRequestUrl(requestUrl);

        if(data!=null)
            super.setBodyBytes(data);
        if(queryParameters!=null)
            super.setQueryParameters(queryParameters);
        if(headers!=null)
            super.setHeaders(headers);
    }

    @Override
    public Response onCompleted(Response response) throws Exception {
        return response;

    }
}
