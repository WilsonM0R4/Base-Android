package com.allegra.handyuvisa.async;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by sfarfan on 07/06/16.
 */
public class ConciergeCodes extends APIInfo {

    public static final String APINAME = "ConciergeCodes";
    public static final String TAG = APINAME;
    public static final String MSG_COUNT = "MSG_COUNT";

    public ConciergeCodes(String query,String url) {
        apiName = APINAME;
        endPoint = url + query;
        Log.d("Concierge EndPoint",endPoint);
        method = "GET";

    }

    public HashMap<String, String> parseData(InputStream inputStream) {
        Log.d(TAG, "Parsing ChatEventsNext");

        HashMap<String, String> result = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        int count = 0;
        try {
            reader.beginArray();
            while (reader.hasNext()) {

              reader.beginObject();
              while (reader.hasNext()) { //&& reader.peek()!= JsonToken.NULL
                  String name =  reader.nextName(), value = "";

                  if (name.equals("value")||name.equals("icon")) {
                      //name = "";
                      Log.e("Yo",name.toString());
                      value = "";
                      reader.skipValue();
                  }  else {
                      Log.e("Yo",name.toString());
                      value = reader.nextString();
                      Log.e("Value",value.toString());
                      result.put(name + count, value);
                  }

                }
                count++;
                reader.endObject();
            }
            reader.endArray();
        } catch (IOException ex) {
            Log.d(TAG, "Something bad");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Can't close incoming onePocketmessage", e);
            }
        }

        result.put(MSG_COUNT, Integer.toString(count));
        return result;

    }

}
