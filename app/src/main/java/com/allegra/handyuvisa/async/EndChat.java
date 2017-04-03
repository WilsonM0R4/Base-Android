package com.allegra.handyuvisa.async;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by lisachui on 10/9/15.
 */
public class EndChat extends APIInfoChat {

    public static final String APINAME = "EndChat";
    public static final String RESP_CODE = "resp_code";
    private static final String TAG = APINAME;

    public EndChat(String eventsUri) {
        apiName = APINAME;
        endPoint = eventsUri + "?v=1";
        method = "POST";

        contentType = "application/xml";
        String buf = "<event type=\"state\"><state>ended</state></event>";
        payloadBytes = buf.getBytes();

//        try {
//            JSONObject textObj = new JSONObject();
//            textObj.put("type", "state");
//            textObj.put("state", "ended");
//
//            JSONObject payload = new JSONObject();
//            payload.put("event", textObj);
//            payloadBytes = payload.toString().getBytes();
//        } catch (JSONException e) {
//            payloadBytes = "Error".getBytes();
//            contentType = "application/plain";
//        }

    }

    protected HashSet<String> getParseRespHeaders() {
        HashSet<String> set = new HashSet<>();
        set.add(RESP_CODE);
        return set;
    }

    protected HashMap<String, String> parseData(InputStream inputStream) {
        return new HashMap<>();
    }
}
