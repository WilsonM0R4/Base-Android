package com.allem.alleminmotion.visacheckout.async;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by lisachui on 10/9/15.
 */
public class CheckAvailability extends APIInfo {

    public static final String APINAME = "CheckAvailability";
    public static final String AVAILABILITY = "availability";

    private static final String TAG = APINAME;

    public CheckAvailability(String uri) {
        apiName = APINAME;
        endPoint = uri + "?v=1";
        method = "GET";


        results = new HashMap<>();
        results.put(AVAILABILITY, null);

    }

    protected HashMap<String, String> parseData(InputStream inputStream) {
        HashMap<String, String> result = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals(AVAILABILITY)) {
                    boolean data = reader.nextBoolean();
                    result.put(name, Boolean.toString(data));
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return result;

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
}
