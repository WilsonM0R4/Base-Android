package com.allegra.handysdk.service;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.ProgressBar;

import com.allegra.handysdk.bean.BeanConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergiofarfan on 20/06/16.
 */
@SuppressLint("NewApi")
public class post_async extends AsyncTask<String, Integer, String>{
    static String action = "", resultString = "";
    Activity activity;
    ProgressBar progressbar;
    Dialog dialog;


    //constructors

    public post_async(Activity act, String action){
        this.activity=act;
        this.action = action;
    }


    // api execution part

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar);
        progressbar = new ProgressBar(activity);
        dialog.addContentView(progressbar, new ActionBar.LayoutParams(50, 50));
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length > 1) {
            invoke(params[0], params[1]);
        } else {
            connect_post(params[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.i("System out", "result: " + resultString);

        try {
            dialog.dismiss();
            System.gc();
            Runtime.getRuntime().gc();

        } catch (Exception e) {
            e.printStackTrace();
        }
        sendResult();
    }

    private void sendResult() {

        try {
            /*if (activity!=null && action.equalsIgnoreCase("Login")) {
                BeanConstants.service.LoginData(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("EditProfile")){
                BeanConstants.service.EditData(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("ForgotPassword")){
                BeanConstants.service.ForgotPasswordData(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("GetCategory")){
                BeanConstants.service.CategoryData(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("GetCustomer")){
                BeanConstants.service.ServiceProviderData(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("timesloat")){
                BeanConstants.service.TimeslotData(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("getBookingListByCustomerId")){
                BeanConstants.service.BookingListData(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("AddServiceBookingDetails")){
                BeanConstants.service.BookingResponse(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("addressList")){
                BeanConstants.service.addressListResponse(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("addAddress")){
                BeanConstants.service.addAddressResponse(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("deleteaddress")){
                BeanConstants.service.DeleteResponse(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("getInvoiceDetails")){
                BeanConstants.service.InvoiceDetailResponse(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("insertfeedback")){
                BeanConstants.service.InsertFeedbackResponse(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("getBookingTrackingLastRecord")){
                BeanConstants.service.ResponseTracking(resultString);
            }else if(activity!=null && action.equalsIgnoreCase("UpdateDeviceToken")){
                BeanConstants.service.Updatetokenresponse(resultString);
            }*/
            //insertfeedback

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*http connection post and get method*/

    private void connect_post(String obj) {
        try {

            String urlStr = obj.replaceAll(" ", "%20").replaceAll("'", "%27");
            Log.d("System out", "Url==> " + urlStr);
            URL url;
            HttpURLConnection connection;
            StringBuffer buffer = null;

            try {
                url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();
                buffer = new StringBuffer();
                InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line = "";
                do {
                    line = buffReader.readLine();
                    if (line != null)
                        buffer.append(line);
                } while (line != null);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultString = buffer.toString();
        } catch (Exception e) {
        }
    }

    public static String invoke(String url, String postString) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String s = "";

        HttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);

        HttpClient client = new DefaultHttpClient(httpParameters);
        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
        client.getParams().setParameter("http.socket.timeout",new Integer(10000));
        client.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
        HttpPost request = new HttpPost(url);
        request.getParams().setParameter("http.socket.timeout",new Integer(10000));

        Log.i("System out", "link : " + url);
        Log.i("System out", "postString : " + postString);

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("json", postString));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                resultString = EntityUtils.toString(httpEntity).toString();
                Log.i("System out", "response is :" + resultString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
