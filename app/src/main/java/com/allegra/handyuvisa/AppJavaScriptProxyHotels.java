package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by RafaelRodriguez on 27/01/16.
 */
public class AppJavaScriptProxyHotels {

    private Activity activity = null;
    private final String  TAG  = "AppJavaScriptProxyHotel";
    public AppJavaScriptProxyHotels(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void postMessage(String message) {
        Log.e(TAG,message);
        Log.e("Repeat message", message);
        /*try {

            JSONObject paymentData= new JSONObject(message);
            Intent intent = new Intent(activity,HotelsPaymentActivity.class);
            intent.putExtra("hotelName",paymentData.getString("hotelName"));
            intent.putExtra("hotelAddress",paymentData.getString("hotelAddress"));
            intent.putExtra("roomType",paymentData.getString("roomType"));
            intent.putExtra("amount",paymentData.getString("amount"));
            intent.putExtra("tax",paymentData.getString("tax"));
            intent.putExtra("fees",paymentData.getString("fees"));
            intent.putExtra("total",paymentData.getString("total"));
            intent.putExtra("currency",paymentData.getString("currency"));
            intent.putExtra("merchantId",paymentData.getString("merchantId"));
            intent.putExtra("reference",paymentData.getString("reference"));
            activity.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        ((HotelSearchActivity)activity).onePocketmessage = message;
        if(checkLogin()) ((HotelSearchActivity) activity).openOnePocket();
    // temporal change

    }

    private boolean checkLogin() {
        if(((VisaCheckoutApp)activity.getApplication()).getIdSession()==null){
            Intent i =new Intent(activity,LoginActivity.class);
            activity.startActivityForResult(i, Constants.ONE_POCKET_NEEDS_LOGIN);
            return false;
        }

        return true;
    }

}
