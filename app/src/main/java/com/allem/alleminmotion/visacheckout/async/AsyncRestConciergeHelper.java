package com.allem.alleminmotion.visacheckout.async;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by sfarfan on 07/06/16.
 */
public class AsyncRestConciergeHelper extends AsyncTask <Void, Void, HashMap<String, String>> {


    private static final String TAG = "AsRestConciergeHelper";
    private static final int APP_SERVER_CONNECTION_TIMEOUT = 30000;
    private APIInfo apiInfo;
    //Constructor
    public AsyncRestConciergeHelper(APIInfo apiInfo){
        this.apiInfo = apiInfo;
    }


    @Override
    protected HashMap<String, String> doInBackground(Void... params) {
        HashMap<String, String> result = null;

        try {
            result = communicate(apiInfo);
        } catch (IOException e) {
            Log.e(TAG, "Can't complete task endpoint", e);
            result = null;
        }
        return result ;
    }


    @Override
    protected void onPostExecute(HashMap<String, String> data) {
        MyBus.getInstance().post(new AsyncTaskMPosResultEvent(data, apiInfo.getApiName()));

    }


    public static HashMap<String, String> communicate(APIInfo apiInfo) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        HttpURLConnection urlConnection = null;
        HashMap<String, String> dataReturn = null;
        StringBuilder tmp = new StringBuilder();

        try {
            URL url = new URL(apiInfo.getEndpoint());
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "URL: " + apiInfo.getEndpoint());

            urlConnection.setRequestMethod(apiInfo.getMethod());
            urlConnection.setConnectTimeout(APP_SERVER_CONNECTION_TIMEOUT);
            urlConnection.setRequestProperty("Content-Type", apiInfo.getContentType());
            urlConnection.setRequestProperty("Accept", apiInfo.getAccept());
            urlConnection.setRequestProperty("Authorization", apiInfo.getAuthorization());

            HashMap<String, String> headers = apiInfo.getHeaders();
            Set<String> keys = headers.keySet();
            Iterator<String> iter = keys.iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                urlConnection.setRequestProperty(key, headers.get(key));
                tmp.append("[" + key + ": " + headers.get(key) + "] ");
            }

            // TODO REMOVE BEFORE RELEASE
            Log.d(TAG, "Request Header: " + tmp);


            if (apiInfo.getMethod().equals("DELETE") || apiInfo.getPayload() == null) {
                urlConnection.connect();
            } else {
                Log.d(TAG, "Payload: " + new String(apiInfo.getPayload()));
                urlConnection.setDoOutput(true);
                out = urlConnection.getOutputStream();
                out.write(apiInfo.getPayload());
            }

            int code = urlConnection.getResponseCode();
            Log.d(TAG, "Response code: " + code);
            if (code == 200 || code == 201) {
                dataReturn = apiInfo.parseData(urlConnection.getInputStream());
                HashSet set = apiInfo.getParseRespHeaders();
                if (set != null && set.contains("resp_code")) {
                    dataReturn.put("resp_code", Integer.toString(code));
                }
                // Log the HTTP response headers from the server
                Iterator<String> respHeaders = urlConnection.getHeaderFields().keySet().iterator();
                tmp.setLength(0);
                while (respHeaders.hasNext()) {
                    String key = respHeaders.next();
                    if (set != null && set.contains(key)) {
                        List<String> list = urlConnection.getHeaderFields().get(key);
                        if (dataReturn == null) {
                            dataReturn = new HashMap<>();
                        }
                        dataReturn.put(key, list.get(0));
                    }
                    tmp.append(key).append(": ")
                            .append(urlConnection.getHeaderFields().get(key))
                            .append("\n");
                }
                Log.d(TAG, "Server's response: (" + tmp + ") ");
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "Can't close input stream", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, "Can't close input stream", e);
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return dataReturn;

    }
}
