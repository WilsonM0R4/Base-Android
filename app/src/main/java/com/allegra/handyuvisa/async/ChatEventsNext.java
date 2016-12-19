package com.allegra.handyuvisa.async;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 * Created by lisachui on 10/9/15.
 */
public class ChatEventsNext extends APIInfoChat {

    public static final String APINAME = "ChatEventsNext";
    public static final String EVENTS_LINK = "events";
    public static final String EVENT_NEXT = "next";
    public static final String INFO_LINK = "info";
    public static final String MSG_START_ID = "@id";
    public static final String MSG_COUNT = "msgCount";
    public static final String CHAT_STATE = "chatState";

    private static final String TAG = APINAME;

    public ChatEventsNext(String nextUri) {
        apiName = APINAME;
        endPoint = nextUri + "&v=1";
        method = "GET";
    }


    public HashMap<String, String> parseData(InputStream inputStream) {
        Log.d(TAG, "Parsing ChatEventsNext");

        HashMap<String, String> result = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("events")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equals("link")) {
                            parseLinkObject(reader, result);
                        } else if (name.equals("event")) {
                            parseEventObject(reader, result);
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
            if (data2.equals(EVENT_NEXT)) {
                result.put(EVENT_NEXT, data1);
            }
            reader.endObject();
        }
        reader.endArray();
    }

    private void parseEventObject(JsonReader reader, HashMap<String, String> result)
                        throws  IOException {
        String name, data1, data2, time, id, text;
        boolean markStartId = false;
        int msgCount = 0;
        JsonToken token = reader.peek();
        if (token == JsonToken.BEGIN_ARRAY) {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                id = null;
                while (reader.hasNext()) {
                    name = reader.nextName();
                    if (name.equals("@id")) {
                        id = reader.nextString();
                    } else if (name.equals("state")) {
                        result.put(CHAT_STATE, reader.nextString());
                    } else {
                        result.put(name + id, reader.nextString());
                        Log.d(TAG, "name: " + name + id + " value: " + result.get(name + id));
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
            result.put(MSG_COUNT, Integer.toString(msgCount));
        } else {
            reader.beginObject();
            while (reader.hasNext()) {
                id = null;
                while (reader.hasNext()) {
                    name = reader.nextName();
                    if (name.equals("@id")) {
                        id = reader.nextString();
                    } else if (name.equals("state")) {
                        result.put(CHAT_STATE, reader.nextString());
                    } else {
                        result.put(name + id, reader.nextString());
                        Log.d(TAG, "name: " + name + id + " value: " + result.get(name + id));
                    }
                }

                result.put(MSG_START_ID, id);
                String type = result.get("@type" + id);
                if (type != null && type.equals("line")) {
                    result.put(CHAT_STATE, "chatting");
                }

                msgCount++;
            }
            reader.endObject();
            result.put(MSG_COUNT, Integer.toString(msgCount));
        }

    }
}
