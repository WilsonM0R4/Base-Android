package com.allegra.handyuvisa.async;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

public class APIInfo {

    private static final String TAG = "APIInfo";
    protected String apiName;
    protected String endPoint;
    protected String authEmail;
    protected String authPassword;
    protected String method;
    protected byte[] payloadBytes;
    protected String accept = "application/json";
    protected String contentType = "application/json";
    protected String authorization = "LivePerson appKey=" + LivePersonConstants.APP_KEY;
    protected HashMap<String, String> headers = new HashMap<>();
    protected HashMap<String, String> results;

    public String getEndpoint() {
        return endPoint;
    }

    public String getMethod() {
        return method;
    }

    public String getRawCredentials() {
        return authEmail + ":" + authPassword;
    }

    public byte[] getPayload() {
        return payloadBytes;
    }

    public String getContentType() {
        return contentType;
    }

    public HashMap<String, String> getHeaders() { return headers; }

    public String getApiName() {
        return apiName;
    }

    public String getAccept() { return accept;}

    public String getAuthorization() { return authorization; }

    protected HashSet<String> getParseRespHeaders() {
        // override for those who are interested in getting results from resp headers;
        return null;
    }

    protected HashMap<String, String> parseData(InputStream inputStream) {

        HashMap<String, String> result = null;
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            result = readMessage(reader);
        } catch (Exception ex) {
            Log.e(TAG, "Can't parse incoming onePocketmessage", ex);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Can't close incoming onePocketmessage", e);
            }
        }
        return result;
    }

    private HashMap<String, String> readMessage(JsonReader reader) throws IOException {

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            Log.d(TAG, name);
            if (results.containsKey(name)) {
                String data = reader.nextString();
                results.put(name, data);
                Log.d(TAG, "Receive HTTP response name: " + name + " value: " + data);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return results;
    }
}
