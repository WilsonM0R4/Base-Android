package com.allegra.handyuvisa.async;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by victor on 04/03/15.
 * com.allem.allemevent.async
 */
public class AsyncJSON extends AsyncTask<String,Void,String> {

    private int codeRequest;
    private String url;

    private AsyncJSON(String url, int codeRequest) {
        this.url=url;
        this.codeRequest=codeRequest;
    }

    public static AsyncJSON getInstance(String url, int codeRequest){
        return new AsyncJSON(url,codeRequest);
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = readJSONFeed(url);
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        MyBus.getInstance().post(new AsyncTaskStringResultEvent(result,codeRequest));
    }

    public String readJSONFeed(String URL) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            } else {
                if (stringBuilder.length()>0) stringBuilder.delete(0,stringBuilder.length()-1);
                stringBuilder.insert(0,"{\"error\":"+statusCode+"}");
                Log.d("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            if (stringBuilder.length()>0) stringBuilder.delete(0,stringBuilder.length()-1);
            stringBuilder.insert(0,"{\"error\":-1}");
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }

}
