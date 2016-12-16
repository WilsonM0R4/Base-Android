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
public class GetBaseResource extends APIInfo {

    public static final String APINAME = "GetBaseResource";
    public static final String CHAT_REQUEST = "chat-request";
    private static final String TAG = APINAME;


    public GetBaseResource() {

        apiName = APINAME;
        endPoint = LivePersonConstants.APP_SERVER_URL + "/api/account/" + LivePersonConstants.SITE_ID + "?v=1";
        method = "GET";
        headers.put("Authorization", "LivePerson appKey=" + LivePersonConstants.APP_KEY);
        results = new HashMap<>();
        results.put(CHAT_REQUEST, null);
    }

    public HashMap<String, String> parseData(InputStream inputStream) {

        HashMap<String, String> result = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                Log.d(TAG,"Entra al 1er while ");
                String name = reader.nextName();
                if (name.equals("account")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        Log.d(TAG,"Entra al 2do while ");
                        name = reader.nextName();
                        if (name.equals("link")) {
                            Log.d(TAG,"Entra al equals link ");
                            reader.beginArray();
                            while (reader.hasNext()) {
                                reader.beginObject();
                                /*name = reader.nextName();
                                Log.d(TAG, "NAme "+name);*/
                                String data1 = reader.nextString();
                                name = reader.nextName();
                                String data2 = reader.nextString();
                                if (data2.equals(CHAT_REQUEST)){
                                    result.put(CHAT_REQUEST, data1);
                                } else {
                                    Log.d(TAG,"ES "+data2);
                                    Log.d(TAG,"data1 "+data1);
                                }
                                reader.endObject();
                            }
                            reader.endArray();
                        } else {
                            reader.skipValue();
                        }
                        Log.d(TAG, "Receive HTTP response name: " + name + " value: ");
                    }
                    reader.endObject();
                } else {
                    Log.d(TAG,"Entra al else de account ");
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace().toString());
            Log.d(TAG, ex.getLocalizedMessage());
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


//            String name = reader.nextName();
//            if (name.equals("account")) {
//                reader.beginObject();
//                while (reader.hasNext()) {
//                    name = reader.nextName();
//                    if (name.equals("link")) {
//                        Log.d(TAG, "now parse link");
//                    }
//                }
//                reader.endObject();
//            } else {
//                //result.add(readError(reader, name));
//            }
//            reader.endObject();
//        } catch (Exception ex) {
//            Log.e(TAG, "Can't parse incoming onePocketmessage", ex);
//        } finally {
//            try {
//                reader.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Can't close incoming onePocketmessage", e);
//            }
//        }
//        Log.d(TAG, "Done parsing transaction history.");
//        return result;
//
//
//    }

//    private HashMap<String, String> readLink(JsonReader reader) throws IOException {
//        reader.beginObject();
//
//    }


}
