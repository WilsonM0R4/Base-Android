package com.allegra.handyuvisa.async;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by lisachui on 10/9/15.
 */
public class AirportCodes extends APIInfo {

    public static final String APINAME = "AirportCodes";
    public static final String RESP_CODE = "RESP_CODE";
    public static final String MSG_COUNT = "MSG_COUNT";
    public static final String NAME = "Name";
    public static final String IATA = "IATA";
    public static final String IATACOUNTRY = "IataCountry";
    public static final String NAMECITY = "NameCity";
    //private static String url = "https://autoaereo.globalti.co/api/values/getinfo?type=json&query=";
    //private static String url = "http://diners.vuelos.ninja/AutoAereo/Index?term=";
    private static final String TAG = APINAME;

    public AirportCodes(String query,String url) {
        apiName = APINAME;
        endPoint = url + query;
       // Log.d("Sergio endPoint",endPoint);
        method = "GET";

    }

    public HashMap<String, String> parseData(InputStream inputStream) {
       // Log.d(TAG, "Parsing ChatEventsNext");

        HashMap<String, String> result = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        int count = 0;
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    String value = reader.nextString();
                    result.put(name + count, value);
                }
                count++;
                reader.endObject();
            }
            reader.endArray();
        } catch (IOException ex) {
            //Log.d(TAG, "Something bad");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
               // Log.e(TAG, "Can't close incoming onePocketmessage", e);
            }
        }

        result.put(MSG_COUNT, Integer.toString(count));
        return result;


    }
}
