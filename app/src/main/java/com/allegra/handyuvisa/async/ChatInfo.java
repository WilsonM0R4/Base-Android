package com.allegra.handyuvisa.async;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by lisachui on 10/9/15.
 */
public class ChatInfo extends APIInfo {

    public static final String APINAME = "ChatInfo";
    public static final String AGENT_NAME = "agentName";
    public static final String START_TIME = "startTime";
    public static final String CHAT_STATE = "state";
    private static final String TAG = APINAME;


    public ChatInfo(String infoUri) {
        apiName = APINAME;
        endPoint = infoUri + "?v=1";
        method = "GET";
    }


    public HashMap<String, String> parseData(InputStream inputStream) {
        Log.d(TAG, "Parsing ChatInfo");

        HashMap<String, String> result = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("info")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equals(AGENT_NAME)) {
                            result.put(AGENT_NAME, reader.nextString());
                        } else if (name.equals(START_TIME)) {
                            result.put(START_TIME, reader.nextString());
                        } else if (name.equals(CHAT_STATE)) {
                            result.put(CHAT_STATE, reader.nextString());
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException ex) {
            Log.d(TAG, "Something bad");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Can't close incoming onePocketmessage", e);
            }
        }
        return result;
    }


//    private void parseEventsObject(JsonReader reader, HashMap<String, String> result)
//            throws IOException {
//        String name, data1, data2;
//        int totalMsg = 0;
//        HashMap<String, String> map = new HashMap<>();
//        reader.beginObject();
//        while (reader.hasNext()) {
//            name = reader.nextName();
//            if (name.equals("link")) {
//                reader.beginArray();
//                while (reader.hasNext()) {
//                    reader.beginObject();
//                    name = reader.nextName();
//                    data1 = reader.nextString();
//                    name = reader.nextName();
//                    data2 = reader.nextString();
//                    if (data2.equals("next")) {
//                        result.put(EVENT_NEXT, data1);
//                    }
//                    reader.endObject();
//                }
//                reader.endArray();
//            } else if (name.equals("event")) {
//                reader.beginArray();
//                while (reader.hasNext()) {
//                    reader.beginObject();
//                    String id = null;
//                    map.clear();
//                    while (reader.hasNext()) {
//                        name = reader.nextName();
//                        if (name.equals("@id")) {
//                            id = reader.nextString();
//                            continue;
//                        }
//                        if (id != null) {
//                            result.put(name + id, reader.nextString());
//                            Log.d(TAG, "name: " + name + id + " value: " + result.get(name+id));
//                        }
//                    }
//                    totalMsg++;
//                    reader.endObject();
//                }
//                reader.endArray();
//            }
//        }
//        reader.endObject();
//
//        result.put("count", Integer.toString(totalMsg));
//    }
}
