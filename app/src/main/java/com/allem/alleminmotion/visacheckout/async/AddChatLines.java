package com.allem.alleminmotion.visacheckout.async;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by lisachui on 10/9/15.
 */
public class AddChatLines extends APIInfo {

    public static final String APINAME = "AddChatLines";
    public static final String RESP_CODE = "resp_code";

    private static final String TAG = APINAME;

    public AddChatLines(String eventsUri, String text) {
        apiName = APINAME;
        endPoint = eventsUri + "?v=1";
        method = "POST";

        contentType = "application/xml";
        StringBuilder buf = new StringBuilder();
        buf.append("<event type=\"line\">" +
                "<text>" + text + "</text></event>");
        payloadBytes = buf.toString().getBytes();
    }

    protected HashSet<String> getParseRespHeaders() {
        HashSet<String> set = new HashSet<>();
        set.add(RESP_CODE);
        return set;
    }

    protected HashMap<String, String> parseData(InputStream inputStream) {
        Log.d(TAG, "Not interested in response body");
        return new HashMap<>();
    }
}
