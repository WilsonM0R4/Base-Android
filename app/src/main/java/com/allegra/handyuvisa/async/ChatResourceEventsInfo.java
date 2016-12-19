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
public class ChatResourceEventsInfo extends APIInfoChat {

    public static final String APINAME = "ChatResourceEventsInfo";
    public static final String EVENTS_LINK = "events";
    public static final String EVENT_NEXT = "next";
    public static final String INFO_LINK = "info";
    public static final String AGENT_NAME = "agentName";
    public static final String CHAT_STATE = "state";
    public static final String MSG_START_ID = "@id";
    public static final String MSG_COUNT = "msgCount";
    private static final String TAG = APINAME;


    public ChatResourceEventsInfo(String chatSessionUri) {
        apiName = APINAME;
        endPoint = chatSessionUri + "?v=1";
        method = "GET";
    }


    public HashMap<String, String> parseData(InputStream inputStream) {
        HashMap<String, String> result = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("chat")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equals("link")) {
                            parseLinkObject(reader, result);
                        } else if (name.equals("events")) {
                            parseEventsObject(reader, result);
                        } else if (name.equals("info")) {
                            parseInfoObject(reader, result);
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

    private void parseLinkObject(JsonReader reader, HashMap<String, String> result)
            throws IOException {

        String name, data1, data2;
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            name = reader.nextName();
            data1 = reader.nextString();
            name = reader.nextName();
            data2 = reader.nextString();
            if (data2.equals(EVENTS_LINK)) {
                result.put(EVENTS_LINK, data1);
            } else if (data2.equals(INFO_LINK)) {
                result.put(INFO_LINK, data1);
            }
            reader.endObject();
        }
        reader.endArray();
    }

    private void parseEventsObject(JsonReader reader, HashMap<String, String> result)
            throws IOException {

        String name, data1, data2;
        boolean markStartId = false;
        int msgCount = 0;
        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            if (name.equals("link")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    name = reader.nextName();
                    data1 = reader.nextString();
                    name = reader.nextName();
                    data2 = reader.nextString();
                    if (data2.equals("next")) {
                        result.put(EVENT_NEXT, data1);
                    }
                    reader.endObject();
                }
                reader.endArray();
            } else if (name.equals("event")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    String id = null;
                    while (reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equals("@id")) {
                            id  = reader.nextString();
                        } else {
                            result.put(name + id, reader.nextString());
                            Log.d(TAG, "name: " + name + id + " value: " + result.get(name+id));
                        }
                    }

                    if (!markStartId) {
                        markStartId = true;
                        result.put(MSG_START_ID, id);
                    }
                    msgCount++;
                    reader.endObject();
                }
                reader.endArray();
            }
        }
        reader.endObject();

        result.put(MSG_COUNT, Integer.toString(msgCount));
    }

    private void parseInfoObject(JsonReader reader, HashMap<String, String> result)
            throws IOException {

        String name;
        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            if (name.equals(AGENT_NAME)) {
                result.put(AGENT_NAME, reader.nextString());
            } else if (name.equals(CHAT_STATE)) {
                result.put(CHAT_STATE, reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }
}