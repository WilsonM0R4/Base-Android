package com.allegra.handyuvisa.async;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by lisachui on 10/9/15.
 */
public class ChatRequest extends APIInfo {

    public static final String APINAME = "ChatRequest";
    public static final String LOCATION = "Location";
    private static final String TAG = APINAME;

    public ChatRequest(String uri) {
        apiName = APINAME;
        endPoint = uri + "?v=1";
        method = "POST";
        //specifySkill();
    }

    protected HashSet<String> getParseRespHeaders() {
        HashSet<String> set = new HashSet<>();
        set.add(LOCATION);
        return set;
    }

    protected HashMap<String, String> parseData(InputStream inputStream) {
        return null;
    }

    // This method is for debugging OneTouchChat functionality. It sends the request to a
    // particular agent with skill "VisaCheckout Test".
    private void specifySkill() {
        try {
            JSONObject textObj = new JSONObject();
            textObj.put("skill", "VisaCheckout Test");
            JSONObject payload = new JSONObject();
            payload.put("request", textObj);
            payloadBytes = payload.toString().getBytes();
        } catch (JSONException e) {
            payloadBytes = "Error".getBytes();
            contentType = "application/plain";
        }
    }
}
